/*
 * Copyright 2016 Crown Copyright
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package gaffer.commonutil;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public abstract class StreamUtil {
    public static final String VIEW = "/view.json";
    public static final String SCHEMA_FOLDER = "/schema/";
    public static final String SCHEMA = SCHEMA_FOLDER + "schema.json";
    public static final String DATA_SCHEMA = SCHEMA_FOLDER + "dataSchema.json";
    public static final String DATA_TYPES = SCHEMA_FOLDER + "dataTypes.json";
    public static final String STORE_SCHEMA = SCHEMA_FOLDER + "storeSchema.json";
    public static final String STORE_TYPES = SCHEMA_FOLDER + "storeTypes.json";
    public static final String STORE_PROPERTIES = "/store.properties";
    public static final String OP_AUTHS = "/opAuths.properties";

    private static final Logger LOGGER = LoggerFactory.getLogger(StreamUtil.class);

    private StreamUtil() {
        // this class should not be instantiated - it contains only util methods and constants.
    }

    public static InputStream view(final Class clazz) {
        return openStream(clazz, VIEW);
    }

    public static InputStream[] schemas(final Class clazz) {
        return openStreams(clazz, SCHEMA_FOLDER);
    }

    public static InputStream schema(final Class clazz) {
        return openStream(clazz, SCHEMA);
    }

    public static InputStream dataSchema(final Class clazz) {
        return openStream(clazz, DATA_SCHEMA);
    }

    public static InputStream dataTypes(final Class clazz) {
        return openStream(clazz, DATA_TYPES);
    }

    public static InputStream storeSchema(final Class clazz) {
        return openStream(clazz, STORE_SCHEMA);
    }

    public static InputStream storeTypes(final Class clazz) {
        return openStream(clazz, STORE_TYPES);
    }

    public static InputStream storeProps(final Class clazz) {
        return openStream(clazz, STORE_PROPERTIES);
    }

    public static InputStream opAuths(final Class clazz) {
        return openStream(clazz, OP_AUTHS);
    }

    public static InputStream view(final Class clazz, final boolean logErrors) {
        return openStream(clazz, VIEW, logErrors);
    }

    public static InputStream[] schemas(final Class clazz, final boolean logErrors) {
        return openStreams(clazz, SCHEMA_FOLDER, logErrors);
    }

    public static InputStream schema(final Class clazz, final boolean logErrors) {
        return openStream(clazz, SCHEMA, logErrors);
    }

    public static InputStream dataSchema(final Class clazz, final boolean logErrors) {
        return openStream(clazz, DATA_SCHEMA, logErrors);
    }

    public static InputStream dataTypes(final Class clazz, final boolean logErrors) {
        return openStream(clazz, DATA_TYPES, logErrors);
    }

    public static InputStream storeSchema(final Class clazz, final boolean logErrors) {
        return openStream(clazz, STORE_SCHEMA, logErrors);
    }

    public static InputStream storeTypes(final Class clazz, final boolean logErrors) {
        return openStream(clazz, STORE_TYPES, logErrors);
    }

    public static InputStream storeProps(final Class clazz, final boolean logErrors) {
        return openStream(clazz, STORE_PROPERTIES, logErrors);
    }

    public static InputStream opAuths(final Class clazz, final boolean logErrors) {
        return openStream(clazz, OP_AUTHS, logErrors);
    }

    public static InputStream[] openStreams(final Class clazz, final String folderPath) {
        return openStreams(clazz, folderPath, false);
    }

    public static InputStream[] openStreams(final Class clazz, final String folderPath, final boolean logErrors) {
        final String folderPathChecked;
        if (null == folderPath || folderPath.endsWith("/")) {
            folderPathChecked = folderPath;
        } else {
            folderPathChecked = folderPath + "/";
        }

        int index = 0;
        final List<String> schemaFiles;
        try {
            schemaFiles = IOUtils.readLines(clazz.getResourceAsStream(folderPathChecked));
        } catch (IOException e) {
            if (logErrors) {
                LOGGER.error("Failed to read schema folder:" + folderPathChecked, e);
                return new InputStream[0];
            } else {
                throw new RuntimeException(e);
            }
        }

        final InputStream[] schemas = new InputStream[schemaFiles.size()];
        for (String schemaFile : schemaFiles) {
            schemas[index] = openStream(clazz, folderPathChecked + schemaFile, logErrors);
            index++;
        }

        return schemas;
    }

    public static InputStream openStream(final Class clazz, final String path) {
        return openStream(clazz, path, false);
    }

    public static InputStream openStream(final Class clazz, final String path, final boolean logErrors) {
        try {
            return clazz.getResourceAsStream(path);
        } catch (final Exception e) {
            if (logErrors) {
                LOGGER.error("Failed to create input stream for " + path, e);
                return null;
            } else {
                throw e;
            }
        }
    }
}