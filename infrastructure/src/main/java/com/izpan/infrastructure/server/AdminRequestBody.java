/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.izpan.infrastructure.server;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.concurrent.ThreadLocalRandom;

public class AdminRequestBody implements Serializable {

    private static final long serialVersionUID = -1288207208017808618L;

    private static final Logger log = LoggerFactory.getLogger(AdminRequestBody.class);

    @Getter
    private final long id;

    @Getter
    private final AdminRequestTypeEnum requestType;

    private byte[] body;

    public AdminRequestBody(long id, AdminRequestTypeEnum requestType) {
        this(id, requestType, 1024);
    }

    public AdminRequestBody(long id, AdminRequestTypeEnum requestType, int size) {
        this.id = id;
        this.body = new byte[size];
        ThreadLocalRandom.current().nextBytes(this.body);
        this.requestType = requestType;
    }

    public void serialize(Object object) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Hessian2Output objectOutputStream = new Hessian2Output(byteArrayOutputStream);
        try {
            objectOutputStream.writeObject(object);
            objectOutputStream.flush();
        } catch (IOException e) {
            log.warn("DynamicTp admin client serialize failed.");
        }
        this.body = byteArrayOutputStream.toByteArray();
    }

    public Object deserialize() {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(body);
        Hessian2Input objectOutputStream = new Hessian2Input(byteArrayInputStream);
        Object object = null;
        try {
            object = objectOutputStream.readObject();
        } catch (IOException e) {
            log.warn("DynamicTp admin client deserialize failed.");
        }
        return object;
    }

}
