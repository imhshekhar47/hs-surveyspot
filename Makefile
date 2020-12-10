VERSION=1.0.0
PROTO_SRC_DIR=./proto

distribute:
	@echo "proto: starting distribution"
	@if [ -d "hs-surveyspot-proto/src/main/proto" ]; then  \
		echo "Copying the latest proto to hs-surveyspot-proto"; \
		cp "${PROTO_SRC_DIR}"/*.proto hs-surveyspot-proto/src/main/proto/; \
		echo "proto distributions: completed"; \
	fi;
	@echo "proto: distribution complete"
	@echo "--------------------------------------"

build_proto: distribute
	@echo "surveyspot-proto: build"
	cd hs-surveyspot-proto \
		&& ./gradlew clean build publishToMavenLocal \
		&& cd - 
	@echo "surveyspot-proto: build complete"
	@echo "--------------------------------------"

build_auth_server: build_proto
	@echo "surveyspot-auth-server: build"
	cd hs-surveyspot-auth-server \
		&& ./gradlew clean build \
		&& cd -
	@echo "surveyspot-auth-server: build complete"
	@echo "--------------------------------------"

build_web: build_proto
	@echo "surveyspot-web: build"
	cd hs-surveyspot-web \
		&& ./gradlew clean build \
		&& cd -
	@echo "surveyspot-web: build complete"
	@echo "--------------------------------------"

auth_server_image: build_auth_server
	@echo "surveyspot-auth-server: image build"
	cd hs-surveyspot-auth-server \
		&& docker build -t imhshekhar47/surveyspot-auth-server:${VERSION} . \
		&& cd -
	@echo "surveyspot-auth-server: image complete"
	@echo "--------------------------------------"

all: distribute
