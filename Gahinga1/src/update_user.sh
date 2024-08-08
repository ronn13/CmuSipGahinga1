<<<<<<< HEAD
#!/bin/bash
# Update user profile in the CSV file
email="$1"
dateOfBirth="$2"
diagnosisDate="$3"
onART="$4"
artStartDate="$5"
countryCode="$6"

if [ -z "$email" ]; then
  echo "Error: Email is required."
  exit 1
fi

awk -F, -v email="$email" -v dob="$dateOfBirth" -v dd="$diagnosisDate" -v art="$onART" -v asd="$artStartDate" -v cc="$countryCode" 'BEGIN{OFS=","} $2 == email {$7 = dob; $8 = dd; $9 = art; $10 = asd; $11 = cc}1' user_sample.csv > temp.csv && mv temp.csv user_sample.csv
=======
#update record in users file
updateuser=$(sed -i '$1s/.*/$2    $3   $4    $5    $6    $7   $8    $9    ${10}    ${11}    ${12}    ${13}/' user-store.txt)
echo $?

>>>>>>> origin/main
