init_str = "INSERT INTO User (Name, DOB, Email, Password, Balance) VALUES"

first_names = ["Emma", "Liam", "Olivia", "Noah", "Ava", "Ethan", "Isabella", "Lucas", "Mia", "Mason"]
last_names = ["Smith", "Johnson", "Williams", "Jones", "Brown", "Davis", "Miller", "Wilson", "Moore", "Taylor"]

import random
from datetime import datetime

def generate_random_dob():
    # Current year
    current_year = datetime.now().year
    
    # Generate a year part of the DOB for someone who is 18-100 years old
    year = random.randint(current_year - 100, current_year - 18)
    
    # Generate a random month and day
    month = random.randint(1, 12)
    
    # Generate a random day based on the month
    # February: consider leap years, 28 or 29 days
    if month == 2:
        if (year % 4 == 0 and year % 100 != 0) or (year % 400 == 0):
            day = random.randint(1, 29)
        else:
            day = random.randint(1, 28)
    # April, June, September, November: 30 days
    elif month in [4, 6, 9, 11]:
        day = random.randint(1, 30)
    # January, March, May, July, August, October, December: 31 days
    else:
        day = random.randint(1, 31)
    
    # Format the DOB as 'YYYY-MM-DD'
    dob = f"{year}-{month:02d}-{day:02d}"
    
    return dob



def user_insert_text(name, dob, email, password, balance):
    return f"('{name}', '{dob}', '{email}', '{password}', {balance}), \n"

for i in range(0, 100):
    init_str += user_insert_text(f"{first_names[i % 10]} {last_names[i // 10]}", generate_random_dob() , f"{first_names[i % 10]}.{last_names[i // 10]}@gmail.com", f"pass{first_names[i%10]}{i}", 100000)


print(init_str[:-3] + ";")