#!/bin/bash

mvn clean install dependency:go-offline -Dmaven.repo.local=repository -Pdev -Dmaven.test.skip=true
