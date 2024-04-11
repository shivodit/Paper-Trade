import pandas as pd 

df = pd.read_csv('ind_nifty100list.csv')
df = df[['Symbol', 'Company Name']]

str = 'INSERT INTO stock (Symbol, Name) VALUES '

for index, row in df.iterrows():
    str += f"('{row['Symbol']}', '{row['Company Name']}'), \n"

with open('stock_entry.txt', 'w') as f:
    f.write(str[:-3] + ";")
    f.flush()