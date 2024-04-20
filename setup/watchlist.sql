    use paper_trade;

    DELIMITER //

    CREATE PROCEDURE PopulateWatchlist()
    BEGIN
        DECLARE counter INT DEFAULT 1;
        DECLARE listName VARCHAR(50);
        
        -- List of different watchlist names
        DECLARE listNames VARCHAR(500);
        SET listNames = 'Tech Stocks,Healthcare Stocks,Energy Stocks,Retail Stocks,Real Estate,Cryptocurrencies,Bonds,Commodities,Foreign Exchange,Emerging Markets';
        
        WHILE counter <= 100 DO
            -- Extract listName based on counter
            SET listName = SUBSTRING_INDEX(SUBSTRING_INDEX(listNames, ',', counter), ',', -1);
            
            INSERT INTO Watchlist (User_ID, List_Name)
            VALUES (counter, listName);
            
            SET counter = counter + 1;
        END WHILE;
        
    END //

    DELIMITER ;

    -- Call the stored procedure to populate the Watchlist table
    CALL PopulateWatchlist();