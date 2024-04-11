with open('insert_stock_price.sql','r') as f:
    # remove duplicate lines
    with open('inser_stock_price_no_duplicate.sql','w') as f2:
        f2.write(''.join(list(set(f.readlines()))))