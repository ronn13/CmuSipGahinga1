#!/bin/bash
# Export user data to a CSV file with headers
echo "UserID,Email,Password,Role,OnART,FirstName,LastName,DOB,DiagnosisDate,ARTStartDate,CountryCode,YearsLeftToLive" > exported_data.csv
tail -n +2 user-store.txt >> exported_data.csv
echo 0