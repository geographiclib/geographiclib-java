USER = karney
STAGE = $(HOME)/web
WEBSTAGE = $(STAGE)/geographiclib-web
VERSION = $(shell grep /version pom.xml | tr '<>\n' / | cut -d/ -f3)
SOURCEDIR = src/main/java/com/github/geographiclib
TESTDIR = src/test/java/com/github/geographiclib

SOURCES= \
$(SOURCEDIR)/Accumulator.java \
$(SOURCEDIR)/Constants.java \
$(SOURCEDIR)/GeoMath.java \
$(SOURCEDIR)/Geodesic.java \
$(SOURCEDIR)/GeodesicData.java \
$(SOURCEDIR)/GeodesicLine.java \
$(SOURCEDIR)/GeodesicMask.java \
$(SOURCEDIR)/GeographicErr.java \
$(SOURCEDIR)/Gnomonic.java \
$(SOURCEDIR)/GnomonicData.java \
$(SOURCEDIR)/Pair.java \
$(SOURCEDIR)/PolygonArea.java \
$(SOURCEDIR)/PolygonResult.java \
$(SOURCEDIR)/package-info.java

TESTS = $(TESTDIR)/GeodesicTest.java

PACKAGES= \
target/GeographicLib-Java-$(VERSION).jar \
target/GeographicLib-Java-$(VERSION)-javadoc.jar \
target/GeographicLib-Java-$(VERSION)-sources.jar

all: $(PACKAGES)

$(PACKAGES) : $(SOURCES) $(TESTS)
	mvn -q package -P release

install: $(PACKAGES)
	mvn -q install -P release

test:
	mvn -q test

distrib-doc: $(PACKAGES)
	rsync -a --delete target/apidocs/ $(WEBSTAGE)/htdocs/Java/$(VERSION)/
	rsync --delete -av -e ssh $(WEBSTAGE)/htdocs/Java $(USER),geographiclib@web.sourceforge.net:./htdocs

deploy:
	mvn -q deploy -P release

sanitize: checktrailingspace checktabs checkblanklines

checktrailingspace:
	@echo "Looking for trailing spaces"
	@git ls-tree -r HEAD --name-only | xargs grep '[	 ]$$' || true

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
	rm -rf $(HOME)/.m2/repository/com/github/geographiclib/GeographicLib-Java/$(VERSION)

checkversion:
	grep "<version>$(VERSION)</version>" pom.xml \
	direct/pom.xml inverse/pom.xml planimeter/pom.xml
