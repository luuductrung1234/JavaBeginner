#!/bin/bash

echo "************************************************"
echo "*                BUILD PROJECT                 *"
echo "************************************************"
echo ""

sh build-learnclasspath.sh

sh build-learnlogging.sh

sh build-learnmultithreadingandconcurrency.sh

sh build-learnruntimetypeandreflection.sh

sh build-learntypemetadatawithannotations.sh

sh build-learnserialization.sh

sh build-learncollectiontostreamsusinglambda.sh

sh build-learnasynchronous.sh
