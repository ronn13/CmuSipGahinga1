#search for existance of country name in user file
searchresult=$(grep $1 life-expectancy.csv)
echo $searchresult
