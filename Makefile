USER = karney
STAGE = $(HOME)/web
WEBSTAGE = $(STAGE)/geographiclib-web
FRSSTAGE = $(STAGE)/geographiclib-files
WEBDEPLOY = $(USER),geographiclib@web.sourceforge.net:./htdocs
FRSDEPLOY = $(USER)@frs.sourceforge.net:/home/frs/project/geographiclib
FULLVERSION = $(shell grep /version pom.xml | tr '<>\n' / | cut -d/ -f3)
VERSION = $(shell echo $(FULLVERSION) | sed s/-.*//)
SOURCEDIR = src/main/java/net/sf/geographiclib
TESTDIR = src/test/java/net/sf/geographiclib

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
target/GeographicLib-Java-$(FULLVERSION).jar \
target/GeographicLib-Java-$(FULLVERSION)-javadoc.jar \
target/GeographicLib-Java-$(FULLVERSION)-sources.jar

all: $(PACKAGES)

$(PACKAGES) : $(SOURCES) $(TESTS)
	mvn -q package -P release

install: $(PACKAGES)
	mvn -q install -P release

test:
	mvn -q test

stage-doc: $(PACKAGES)
	rsync -a --delete target/apidocs/ $(WEBSTAGE)/htdocs/Java/$(VERSION)/

deploy-doc:
	rsync --delete -av -e ssh $(WEBSTAGE)/htdocs/Java $(WEBDEPLOY)/

stage-dist: $(PACKAGES)
	cp -p $^ distrib-Java
	rsync --delete -av --exclude '*~' --delete-excluded distrib-Java $(FRSSTAGE)/

deploy-dist:
	rsync --delete -av $(FRSSTAGE)/distrib-Java $(FRSDEPLOY)

deploy:
	mvn -q deploy -P release

sanitize: checktrailingspace checktabs checkblanklines

checktrailingspace:
	@echo "Looking for trailing spaces"
	@git ls-files | xargs grep '[	 ]$$' || true

checktabs:
	@echo "Looking for tabs"
	@git ls-files | grep -v Makefile | \
	xargs grep '	' || true

checkblanklines:
	@echo "Looking for extra blank lines"
	@git ls-files | \
	while read f; do tr 'X\n' 'YX' < $$f | \
	egrep '(^X|XXX|XX$$|[^X]$$)' > /dev/null && echo $$f; done || true

clean:
	mvn clean

reallyclean: clean
	rm -rf $(HOME)/.m2/repository/net/sf/geographiclib/GeographicLib-Java/$(FULLVERSION)

checkversion:
	grep "<version>$(FULLVERSION)</version>" pom.xml \
	direct/pom.xml inverse/pom.xml planimeter/pom.xml
