#/bin/bash

# check nuumber arg 
if [ $# -ne 1 ]; then
    echo "use: $0 <number>"
    exit 1
fi

num=$1
PRINT=false
DOWNLOAD=true
DB=true
OUTPUT=$(pwd)

echo "$num pages spotlight."

for (( i=1; i<=$num; i++ ))
do
    echo "Page $i: run java file..."
    
    java -jar spotlight.jar $i $PRINT $DOWNLOAD $DB $OUTPUT
    
    echo "1 minute wait..."
    sleep 60    
done

echo "Done."

