/* 
 * Copyright 2016 Patrizio Bruno <desertconsulting@gmail.com>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.dstc.mochatemplate.parser.node;

import net.dstc.mochatemplate.parser.Parser;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import org.jsoup.nodes.Node;

/**
 * This attribute parser supports data-for attribute. It loops through a list
 * replicating the parent node once per every element of the list. If the
 * expression returns a string, it iterates through every single character. If
 * the expression returns an object it iterates through object properties. It
 * set a variable in the current Javascript context, available only to
 * javascript expressions nested into the parent node. Optionally it can set a
 * variable containing the current index: a number for lists, arrays and strings
 * and a string for objects.
 *
 * @author Patrizio Bruno <desertconsulting@gmail.com>
 */
public class ForAttributeParser extends TemplateAttributeParser {

    private static final String SUPPORTED_FIELD = "for";

    /**
     * .ctor
     *
     * @param engine javascript engine
     */
    public ForAttributeParser(ScriptEngine engine) {
        super(engine);
    }

    @Override
    public boolean eval(TemplateNode dataNode, AttributeParserArguments args,
            Parser parser)
            throws ScriptException, UnsupportedEncodingException {

        if (args.length() == 1 || args.length() == 2) {
            String varName = args.getArgs()[0];
            String indexName = args.length() == 2 ? args.getArgs()[1] : null;
            Bindings bindings = dataNode.getBindings();
            Node node = dataNode.getNode();
            Object exprResult;
            String expression = args.getExpression();

            // if the expression is a range-descriptor, it must not be evaluated as Javascript.
            // I adopted this syntax because it's cleaner than a Javascript solution
            // such as Array.apply(null, { length: 5 }).map(Number.call, Number)
            if (RangeIterator.isRange((String) expression)) {
                exprResult = expression;
            } else if (bindings == null) {
                exprResult = engine.eval(args.getExpression());
            } else {
                exprResult = engine.eval(args.getExpression(), bindings);
            }

            Node prev = node.previousSibling();
            node.removeAttr(args.getAttributeName());

            Iterator<Map.Entry> values = null;
            if (exprResult != null) {
                if (exprResult instanceof List) {
                    values = new ListIterator((Collection) exprResult);
                } else if (exprResult instanceof Map) {
                    // Actually only Nashorn - thus JDK8 - does support Map types. JDK7 will never use MapIterator
                    values = new MapIterator((Map) exprResult);
                } else if (exprResult instanceof String) {
                    if (RangeIterator.isRange((String) exprResult)) {
                        values = new RangeIterator((String) exprResult);
                    } else {
                        values = new StringIterator((String) exprResult);
                    }
                } else if (exprResult instanceof Double) {
                    values = new SingleElementIterator(exprResult);
                } else if (exprResult instanceof Object) {
                    values = new ObjectIterator(exprResult);
                } else {
                    // used ScriptEngines can produce a different type?
                    values = new SingleElementIterator(exprResult);
                }
            }

            while (values != null && values.hasNext()) {
                Map.Entry entry = values.next();
                if (bindings != null) {
                    bindings.put(varName, entry.getValue());
                    if (indexName != null) {
                        bindings.put(indexName, entry.getKey());
                    }
                }

                Node n = node.clone();
                prev.after(n);

                parser.parseNode(n, bindings);
                if (n.parent() != null) {
                    prev = n;
                }
            }
            node.remove();
            return false;
        } else {
            throw new ScriptException(
                    "template error: wrong number of arguments to data-for");
        }
    }

    @Override
    public String supportedAttr() {
        return SUPPORTED_FIELD;
    }

    /**
     * Iterates through the properties of an object. Each entry has property
     * name as key and property value as a value.
     */
    private class ObjectIterator implements Iterator<Map.Entry> {

        private final List<Map.Entry<String, Object>> list = new ArrayList<>();
        private final Iterator<Map.Entry<String, Object>> iterator;

        /**
         * Initialize a new instance of {@link ObjectIterator), creating an iterator
         * over {@code obj} properties.
         *
         * @param obj source of the properties
         */
        public ObjectIterator(Object obj) {
            Class cl = obj.getClass();
            for (Method getter : cl.getMethods()) {
                if (Modifier.isPublic(getter.getModifiers())
                        && getter.getParameterTypes().length == 0) {
                    String name = getter.getName();
                    if (name.length() > 3
                            && !"getClass".equals(name)
                            && (name.startsWith("get")
                            || (name.startsWith("is")
                            && getter.getReturnType() == Boolean.class))) {
                        try {
                            int pos = name.startsWith("get") ? 3 : 2;
                            name = name.substring(pos, pos + 1).toLowerCase()
                                    + name.
                                    substring(pos + 1);
                            Object value = getter.invoke(obj);
                            list.add(new AbstractMap.SimpleEntry<>(name, value));
                        } catch (IllegalAccessException |
                                IllegalArgumentException |
                                InvocationTargetException ex) {
                            Logger.getLogger(ForAttributeParser.class.getName()).
                                    log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }

            for (Field field : cl.getFields()) {
                if (Modifier.isPublic(field.getModifiers())) {
                    try {
                        Object value = field.get(obj);
                        list.add(new AbstractMap.SimpleEntry<>(field.getName(),
                                value));
                    } catch (IllegalArgumentException |
                            IllegalAccessException ex) {
                        Logger.getLogger(ForAttributeParser.class.getName()).
                                log(Level.SEVERE, null, ex);
                    }
                }
            }

            iterator = list.iterator();
        }

        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        @Override
        public Map.Entry<String, Object> next() {
            return iterator.next();
        }

        @Override
        public void remove() {
            iterator.remove();
        }
    }

    private class MapIterator implements Iterator<Map.Entry> {

        private final Iterator iterator;

        public MapIterator(Map map) {
            iterator = map.entrySet().iterator();
        }

        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        @Override
        public Map.Entry next() {
            return (Map.Entry) iterator.next();
        }

        @Override
        public void remove() {
            iterator.remove();
        }
    }

    /**
     * Iterates through a string's characters.
     */
    private class StringIterator implements
            Iterator<Map.Entry> {

        private int idx = -1;
        private final char[] characters;

        public StringIterator(String string) {
            if (string != null && string.length() > 0) {
                characters = string.toCharArray();
            } else {
                characters = new char[0];
            }
        }

        @Override
        public boolean hasNext() {
            return idx < characters.length - 1;
        }

        @Override
        public Map.Entry<Integer, Character> next() {
            if (hasNext()) {
                idx++;
                return new AbstractMap.SimpleEntry<>(idx, characters[idx]);
            }
            throw new NoSuchElementException();
        }

        @Override
        public void remove() {
            if (idx == -1) {
                throw new IllegalStateException();
            }
            idx--;
        }
    }

    /**
     * Iterates over a list made of a single element.
     */
    private class SingleElementIterator implements
            Iterator<Map.Entry> {

        private boolean finished = false;
        private final Object single;

        public SingleElementIterator(Object single) {
            this.single = single;
        }

        @Override
        public boolean hasNext() {
            return !finished;
        }

        @Override
        public Map.Entry<Integer, Object> next() {
            if (!finished) {
                finished = true;
                return new AbstractMap.SimpleEntry<>(0, single);
            }
            throw new NoSuchElementException();
        }

        @Override
        public void remove() {
            if (finished) {
                finished = false;
            } else {
                throw new IllegalStateException();
            }
        }
    }

    private static final Pattern RANGE_PATTERN = Pattern.compile(
            "([0-9]+)\\.\\.\\.([0-9]+)");

    /**
     * Iterates over a range of values.
     */
    private static class RangeIterator implements
            Iterator<Map.Entry> {

        private final String range;
        private Integer first;
        private Integer last;
        private Integer current;
        private int idx = -1;

        /**
         * Initialize a new instance of {@link RangeIterator}, creating a new
         * iterator over a list of numeric values within a range defined by the
         * string {@code range}. {@code range} must have the syntax
         * [0-9]+...[0-9]+: eg. 1...10
         *
         * @param range a string describing a numeric range
         */
        public RangeIterator(String range) {
            this.range = range;
            parseRange();
        }

        /**
         * Utility to check if a string defines a numeric range.
         *
         * @param range a string that may contain a numeric range definition.
         * @return true if the string defines a numeric range.
         */
        public static boolean isRange(String range) {
            return RANGE_PATTERN.matcher(range).matches();
        }

        @Override
        public boolean hasNext() {
            return current <= last;
        }

        @Override
        public Map.Entry<Integer, Integer> next() {
            if (hasNext()) {
                return new AbstractMap.SimpleEntry<>(++idx, current++);
            }
            throw new NoSuchElementException();
        }

        @Override
        public void remove() {
            if (current > first) {
                current--;
                idx--;
            } else {
                throw new IllegalStateException();
            }
        }

        private void parseRange() {
            Matcher matcher = RANGE_PATTERN.matcher(range);
            if (matcher.find()) {
                current = first = Integer.parseInt(matcher.group(1));
                last = Integer.parseInt(matcher.group(2));
            }
        }
    }

    /**
     * Iterates over a list of objects.
     */
    private class ListIterator implements Iterator<Map.Entry> {

        private final Iterator iterator;
        private int idx = -1;

        public ListIterator(Collection list) {
            this.iterator = list.iterator();
        }

        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        @Override
        public Map.Entry<Integer, Object> next() {
            if (iterator.hasNext()) {
                return new AbstractMap.SimpleEntry<>(++idx, iterator.next());
            }
            throw new NoSuchElementException();
        }

        @Override
        public void remove() {
            if (idx == -1) {
                throw new IllegalStateException();
            }
            iterator.remove();
            idx--;
        }
    }
}
