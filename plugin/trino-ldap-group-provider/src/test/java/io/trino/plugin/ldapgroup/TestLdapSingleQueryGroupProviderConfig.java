/*
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
package io.trino.plugin.ldapgroup;

import com.google.common.collect.ImmutableMap;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static io.airlift.configuration.testing.ConfigAssertions.assertFullMapping;
import static io.airlift.configuration.testing.ConfigAssertions.assertRecordedDefaults;
import static io.airlift.configuration.testing.ConfigAssertions.recordDefaults;

final class TestLdapSingleQueryGroupProviderConfig
{
    @Test
    void testDefaults()
    {
        assertRecordedDefaults(recordDefaults(LdapSingleQueryGroupProviderConfig.class)
                .setLdapUserMemberOfAttribute("memberOf"));
    }

    @Test
    void testExplicitPropertyMapping()
    {
        Map<String, String> properties = ImmutableMap.of(
                "ldap.user-member-of-attribute", "customMemberOf");

        LdapSingleQueryGroupProviderConfig expected = new LdapSingleQueryGroupProviderConfig()
                .setLdapUserMemberOfAttribute("customMemberOf");

        assertFullMapping(properties, expected);
    }
}
