/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *  <p/>
 *  http://www.apache.org/licenses/LICENSE-2.0
 *  <p/>
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.github.mongo.labs.api.com.github.mongo.labs.util;

import com.github.mongo.labs.helper.MongoQueryService;
import org.junit.ComparisonFailure;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.net.UnknownHostException;

public class QueryCheckRule implements TestRule {

    private final MongoQueryService queryService = new MongoQueryService();

    private boolean alreadyInit = false;

    @Override
    public Statement apply(Statement statement, Description description) {
        ExpectedQuery annotation = description.getAnnotation(ExpectedQuery.class);
        if(annotation == null) {
            return statement;
        }
        if(!alreadyInit) {
            try {
                queryService.init();
                alreadyInit = true;
            } catch (UnknownHostException e) {
                // Damn it, go a problem
                // so ignoring it
                return statement;
            }

        }
        return new QueryCheckRuleStatement(statement, annotation.query());
    }

    private class QueryCheckRuleStatement extends Statement {
        private final Statement statement;
        private final String query;

        public QueryCheckRuleStatement(Statement statement, String query) {
            this.statement = statement;
            this.query = query;
        }

        @Override
        public void evaluate() throws Throwable {
            statement.evaluate();
            String lastQuery = queryService.lastQuery();
            if(!lastQuery.replace(" ", "").equalsIgnoreCase(query.replace(" ", ""))) {
                throw new ComparisonFailure("Your query doesn't match expected query !", query, lastQuery);
            }
        }
    }
}
