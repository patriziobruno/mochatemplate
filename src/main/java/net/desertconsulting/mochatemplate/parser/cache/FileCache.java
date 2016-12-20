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
package net.desertconsulting.mochatemplate.parser.cache;

import java.net.URI;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This utility class serves as cache for files. Used to cache external script
 * files and external templates.
 *
 * @author Patrizio Bruno {@literal <desertconsulting@gmail.com>}
 * @param <T> type of the file content to be cached
 */
public class FileCache<T> {

    private final ConcurrentHashMap<URI, CacheItem<T>> cache
            = new ConcurrentHashMap<>();

    /**
     * Retrieves a file content from the cache.
     * 
     * @param key file expected to be cached
     * @return content of the file or null if the file has not been cached
     */
    public T get(CacheFile key) {
        T rv = null;
        URI uri = key.getUri();

        if (cache.containsKey(uri)) {
            CacheItem<T> cachedItem = cache.get(uri);
            if (cachedItem.cacheFile.equals(key)) {
                rv = cachedItem.content;
            } else {
                cache.remove(uri);
            }
        }
        return rv;
    }

    /**
     * Puts a file content into the cache.
     * 
     * @param file file to be cached
     * @param content file content to be cached
     */
    public void put(CacheFile file, T content) {
        cache.put(file.getUri(), new CacheItem<>(file, content));
    }

    /**
     * Check if a given file has been cached.
     * 
     * @param key file expected to be cached
     * @return true if the file has been cached, false otherwise
     */
    public boolean containsKey(CacheFile key) {
        return get(key) != null;
    }

    /**
     * This class is a container for cached items.
     * 
     * @param <T> type of the file content 
     */
    private class CacheItem<T> {

        public CacheFile cacheFile;
        public T content;

        public CacheItem(CacheFile cacheFile, T content) {
            this.cacheFile = cacheFile;
            this.content = content;
        }
    }
}
