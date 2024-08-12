#search for existance of country name in user file
searchresult=$(grep -w $1 life-expectancy.csv)
echo $searchresult
