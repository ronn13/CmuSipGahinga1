#search for existance of value in user file
searchresult=$(awk "/$1/ && /Admin/" user_sample.csv)
echo $searchresult
