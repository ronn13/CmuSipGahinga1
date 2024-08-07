#search for existance of uid in user file
searchresult=$(grep -w $1 user-store.txt)
echo $searchresult
