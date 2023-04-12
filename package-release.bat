@echo -----------------------------------------------------------------------------
@echo =====START PACKAGE RELEASE=====
@echo -----------------------------------------------------------------------------
mvn clean package -Dmaven.test.skip=true -Prelease
@pause