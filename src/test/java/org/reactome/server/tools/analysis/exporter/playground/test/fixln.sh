for i in $(ls | grep "^R.*.json");
do
    name = $(cat ${i} | grep "^[0-9].*.json");
    rm ${i};
    cp -l ${i};
done