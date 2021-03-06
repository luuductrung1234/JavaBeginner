#!/bin/bash

echo "************************************************"
echo "*             BUILD ALL PROJECTS               *"
echo "************************************************"
echo ""

sh ./build/build-common.sh

sh ./build/build-learnclasspath.sh

sh ./build/build-learnlogging.sh

sh ./build/build-learnmultithreadingandconcurrency.sh

sh ./build/build-learnruntimetypeandreflection.sh

sh ./build/build-learntypemetadatawithannotations.sh

sh ./build/build-learnserialization.sh

sh ./build/build-learncollectiontostreamsusinglambda.sh

sh ./build/build-learnasynchronous.sh

sh ./build/build-dataprocessinginjava8.sh

echo "************************************************"
echo "*               BUILD SUCCEEDED                *"
echo "************************************************"
echo ""