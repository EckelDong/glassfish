/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2010 Sun Microsystems, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 *
 * Contributor(s):
 *
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */
package org.glassfish.admin.rest;

import com.sun.jersey.api.client.ClientResponse;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author jasonlee
 */
public class ResourceRefTest extends RestTestBase {
    public static final String URL_CREATE_INSTANCE = BASE_URL_DOMAIN + "/create-instance";
    public static final String URL_JDBC_RESOURCE = BASE_URL_DOMAIN + "/resources/jdbc-resource";
    public static final String URL_RESOURCE_REF = BASE_URL_DOMAIN + "/servers/server/server/resource-ref";
    public static final String URL_DELETE_INSTANCE = "";

    @Test
    public void testCreatingResourceRef() {
        final String instanceName = "instance_" + generateRandomString();
        final String jdbcResourceName = "jdbc_" + generateRandomString();
        Map<String, String> newInstance = new HashMap<String, String>() {{
            put("id", instanceName);
            put("node", "localhost");
        }};
        Map<String, String> jdbcResource = new HashMap<String, String>() {{
            put("id", jdbcResourceName);
            put("connectionpoolid", "DerbyPool");
            put("target", instanceName);
        }};
        Map<String, String> resourceRef = new HashMap<String, String>() {{
            put("id", jdbcResourceName);
            put("target", "server");
        }};

        try {
            ClientResponse response = post(URL_CREATE_INSTANCE, newInstance);
            assertTrue(isSuccess(response));

            response = post(URL_JDBC_RESOURCE, jdbcResource);
            assertTrue(isSuccess(response));
 
            response = post(URL_RESOURCE_REF, resourceRef);
            assertTrue(isSuccess(response));
        } finally {
            ClientResponse response = delete(BASE_URL_DOMAIN + "/servers/server/" + instanceName + "/resource-ref/" + jdbcResourceName, new HashMap<String, String>() {{ put("target", instanceName); }});
            assertTrue(isSuccess(response));
            response = get(BASE_URL_DOMAIN + "/servers/server/" + instanceName + "/resource-ref/" + jdbcResourceName);
            assertFalse(isSuccess(response));

            response = delete(URL_JDBC_RESOURCE + "/" + jdbcResourceName);
            assertTrue(isSuccess(response));
            response = get(URL_JDBC_RESOURCE + "/" + jdbcResourceName);
            assertFalse(isSuccess(response));
            
            response = delete(BASE_URL_DOMAIN + "/servers/server/" + instanceName + "/delete-instance");
            assertTrue(isSuccess(response));
            response = get(BASE_URL_DOMAIN + "/servers/server/" + instanceName);
            assertFalse(isSuccess(response));
        }
    }
}
