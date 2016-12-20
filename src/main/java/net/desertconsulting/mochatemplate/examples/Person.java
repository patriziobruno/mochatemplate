package net.desertconsulting.mochatemplate.examples;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Person {

    private static final List<Person> STATIC_PERSON = new ArrayList<Person>() {
        private static final long serialVersionUID = 8751914654566394665L;

        {
            add(new Person(0, "Mario Rossi", "", false, 1));
            add(new Person(1, "John Doe", "Jane Doe", true, 3));
            add(new Person(2, "Jean Dupont", "Erika Mustermann", true, 0));
        }
    };

    private String name;
    private boolean married;
    private String spouse;
    private List<String> children;
    private int id;

    public Person() {
        this.name = "";
        this.married = false;
        this.spouse = "";
        children = Collections.emptyList();
    }

    public Person(int id, String name, String spouse, boolean isMarried, int nChildren) {
        super();
        this.name = name;
        this.spouse = spouse;
        this.married = isMarried;
        this.id = id;
        children = new ArrayList<>();
        for (int z = 0; z < nChildren; z++) {
            children.add("Children " + z);
        }
    }

    public static Person lookup(String id) {
        if (id != null) {
            int _id = Integer.valueOf(id);
            if (_id <= STATIC_PERSON.size()) {
                return STATIC_PERSON.get(_id - 1);
            } else {
                return new Person(0, "Empty Name", "Empty spouse", false, 0);
            }
        } else {
            return new Person(0, "Empty Name", "Empty spouse", false, 0);
        }
    }

    public static List<Person> search(String name) {
        List<Person> rv;
        if (name != null) {
            rv = new ArrayList<>();
            for (Person person : STATIC_PERSON) {
                if (person.getName().toLowerCase().contains(name.toLowerCase())) {
                    rv.add(person);
                }
            }
        } else {
            rv = STATIC_PERSON;
        }
        return rv;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpouse() {
        return this.spouse;
    }

    public void setSpouse(String spouse) {
        this.spouse = spouse;
    }

    public boolean isMarried() {
        return married;
    }

    public void setMarried(boolean married) {
        this.married = married;
    }

    public List<String> getChildren() {
        return children;
    }

    public void setChildren(List<String> children) {
        this.children = children;
    }

    @Override
    public String toString() {
        return "Person [name=" + name + ", married=" + married + ", spouse="
                + spouse + ", children=" + children + "]";
    }
}
