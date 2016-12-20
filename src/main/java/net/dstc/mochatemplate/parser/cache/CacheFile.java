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
package net.dstc.mochatemplate.parser.cache;

import java.io.File;
import java.net.URI;
import java.util.Objects;

/**
 * This class serves as container for file information on files to be cached.
 * 
 * @author Patrizio Bruno <desertconsulting@gmail.com>
 */
public class CacheFile {

    private final URI uri;
    private final long lastModified;
    public final File file;

    public CacheFile(URI uri) {
        this.uri = uri;
        this.file = new File(uri);
        lastModified = this.file.lastModified();
    }

    public URI getUri() {
        return uri;
    }

    public File getFile() {
        return this.file;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 43 * hash + Objects.hashCode(this.uri);
        hash = 43 * hash + (int) (this.lastModified ^ (this.lastModified
                >>> 32));
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CacheFile other = (CacheFile) obj;
        if (this.lastModified != other.lastModified) {
            return false;
        }

        if (!Objects.equals(this.uri, other.uri)) {
            return false;
        }
        return true;
    }
}
