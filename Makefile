USER=karney
STAGE=$(HOME)/web
WEBSTAGE=$(STAGE)/geographiclib-web
VERSION=$(shell grep /version pom.xml | tr '<>\n' / | cut -d/ -f3)

all:
	mvn -q package -P release
	rsync -a --delete target/apidocs/ $(WEBSTAGE)/htdocs/Java/$(VERSION)/

depoly:
	mvn -q deploy -P release

sanitize: checktrailingspace checktabs checkblanklines

checktrailingspace:
	@echo "Looking for trailing spaces"
	@git ls-tree -r HEAD --name-only | xargs grep ' $$' || true

checktabs:
	@echo "Looking for tabs"
	@git ls-tree -r HEAD --name-only | grep -v Makefile | \
	xargs grep '	' || true

checkblanklines:
	@echo "Looking for extra blank lines"
	@git ls-tree -r HEAD --name-only | \
	while read f; do tr 'X\n' 'YX' < $$f | \
	egrep '(^X|XXX|XX$$|[^X]$$)' > /dev/null && echo $$f; done || true

clean:
	mvn clean

reallyclean: clean
	rm -rf $$HOME/.m2/repository/net/sf/geographiclib/GeographicLib-Java/$(VERSION)

checkversion:
	grep "<version>$(VERSION)</version>" pom.xml \
	direct/pom.xml inverse/pom.xml planimeter/pom.xml
