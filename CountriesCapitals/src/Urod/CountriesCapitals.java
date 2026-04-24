package Urod;
//imports 
import java.awt.Image;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class CountriesCapitals extends javax.swing.JFrame {

    // ===== DYNAMIC BASE PATH =====
    // user.dir to get the current directory -- previously tryed with user.home
    private static final String BASE_PATH = System.getProperty("user.dir");
    
    // ===== DATABASE CONNECTION =====
    //setting the path to absolute
    private static final String DB_PATH = BASE_PATH + "/Data/Countries.accdb";
    // The JDBC URL tells Java which driver to use (UCanAccess) and where the file is located
    private static final String DB_URL = "jdbc:ucanaccess://" + DB_PATH;
    
    // ===== IMAGES PATH =====
    //stored images for flags and icon in /images/
    private static final String IMG_PATH = BASE_PATH + "/images/";
    //this keeps the link to the database open while the app is running
    private Connection conn;
     // DefaultListModel allows us to easily add, remove, and clear items in a JList dynamically.
    // One model for the list of countries to compare, one for the statistics output.
    private final DefaultListModel<String> countriesListModel = new DefaultListModel<>();
    private final DefaultListModel<String> statisticsListModel = new DefaultListModel<>();

    public CountriesCapitals() {
        initComponents();
         // ===== SET FRAME ICON =====
        // Looks for "icon.png" in your images folder and sets it as the window icon
        java.io.File iconFile = new java.io.File(IMG_PATH + "icon.png");
        if (iconFile.exists()) {
            Image icon = new ImageIcon(iconFile.getAbsolutePath()).getImage();
            this.setIconImage(icon);
        }
        // Set up list models
        lstCountries.setModel(countriesListModel);
        lstStatistics.setModel(statisticsListModel);
        // Center the flag image within its label area
        lblFlag.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblLoaded.setText("Connecting to database...");
        
        // Connect and load data
        connectToDatabase();
    }

    // ===== DATABASE METHODS =====
    //connects to the accdb using Ucanaccess driver, if successful it updates the label and calls for the loadCountries()
    //if it fails it shows an error msg
    private void connectToDatabase() {
        try {
            conn = DriverManager.getConnection(DB_URL);
            lblLoaded.setText("Database connected successfully!");
            loadCountries();//call method to populate the dropdown
        } catch (SQLException e) {
            lblLoaded.setText("Database connection FAILED!");
            JOptionPane.showMessageDialog(this, 
                "Could not connect to database:\n" + e.getMessage(),
                "Connection Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    //method to load all country names from the database into the comboBox(dropdown)
    //also there is an alfabetic order to make it easyer when searching
    private void loadCountries() {
        cmbCountries.removeAllItems();//make sure the dropdown is clear
        try {
            Statement stmt = conn.createStatement();//Create a statement to execute the query
            //execute query and order countryName A to Z
            ResultSet rs = stmt.executeQuery("SELECT CountryName FROM CountryCapital ORDER BY CountryName");
            //loop through all results row by row ( line by line )
            while (rs.next()) {
                String country = rs.getString("CountryName");
                
               //check if the country name is not null or if you have empty spaces in it
                if (country != null && !country.trim().isEmpty()) {
                    cmbCountries.addItem(country.trim());//add the item to the dropdown (list)
                }
            }
            rs.close();
            stmt.close();
            //update the label and show how many countries are added
            lblLoaded.setText("Loaded " + cmbCountries.getItemCount() + " countries.");
        } catch (SQLException e) {
            lblLoaded.setText("Error loading countries.");
            e.printStackTrace();
        }
    }
    //method to safely return the string from the DB if the field is missing or empty values 
    private String getStringSafe(ResultSet rs, String columnName, String defaultValue) {
        try {
            String value = rs.getString(columnName);
            if (value == null || value.trim().isEmpty()) {
                return defaultValue;
            }
            return value.trim();
        } catch (SQLException e) {
            return defaultValue;//return default value so it doesnt crash
        }
    }
    //method to safely return a long number, a missing number or a number equal to 0
    private long getLongSafe(ResultSet rs, String columnName, long defaultValue) {
        try {
            long value = rs.getLong(columnName);
            if (rs.wasNull() || value == 0) {
                return defaultValue;
            }
            return value;
        } catch (SQLException e) {
            return defaultValue;
        }
    }

    // ===== FLAG LOADING METHOD (USING STANDARD JAVA FOR .PNG) =====
    //it loads a flag image based on country code Romania = Ro.ToLowerCase() + .png
    private void loadFlag(String countryCode) {
        if (countryCode == null || countryCode.trim().isEmpty()) {
            lblFlag.setIcon(null);
            lblFlag.setText("No Flag");
            return;
        }
        
        // Using .png because standard Java cannot read .svg files tried with Salamander library and failed
        String filePath = IMG_PATH + countryCode.toLowerCase() + ".png";
        File file = new File(filePath);
        //check if the image exists if not ... return Flag missing
        if (!file.exists()) {
            lblFlag.setIcon(null);
            lblFlag.setText("Flag missing");
            return;
        }
        
        try {
            //create image icon
            ImageIcon icon = new ImageIcon(filePath);
            //scale image to fit the size of lblFlag
            Image img = icon.getImage().getScaledInstance(lblFlag.getWidth(), lblFlag.getHeight(), Image.SCALE_SMOOTH);
            //set scaled image back onto the label
            lblFlag.setIcon(new ImageIcon(img));
            lblFlag.setText("");//clear text , had an issue with "Flag missing" persisting 
        } catch (Exception e) {
            lblFlag.setIcon(null);
            lblFlag.setText("Error loading flag");
            e.printStackTrace();
        }
    }

    // close the program and exit the application
    private void btnExitActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            if (conn != null && !conn.isClosed()) { conn.close(); }
        } catch (SQLException e) { e.printStackTrace(); }
        System.exit(0);
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        lblCapital = new javax.swing.JLabel();
        cmbCountries = new javax.swing.JComboBox<>();
        btnGetCountry = new javax.swing.JButton();
        lblFlag = new javax.swing.JLabel();
        lblLoaded = new javax.swing.JLabel();
        btnExit = new javax.swing.JButton();
        btnAddToCompare = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        lstCountries = new javax.swing.JList<>();
        jScrollPane3 = new javax.swing.JScrollPane();
        lstStatistics = new javax.swing.JList<>();
        btnClearList = new javax.swing.JButton();
        btnShowStatistics = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Countries And Their Capitals");

        jPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel1.setForeground(new java.awt.Color(153, 153, 153));

        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel1.setText("Countries And Their Capitals");

        jLabel2.setText("Select a country :");

        lblCapital.setText("Select a country and i will display its capital.");
        lblCapital.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        btnGetCountry.setText("Get Country");
        btnGetCountry.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGetCountryActionPerformed(evt);
            }
        });

        lblFlag.setText("jLabel4");

        lblLoaded.setText("jLabel5");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblLoaded, javax.swing.GroupLayout.PREFERRED_SIZE, 330, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel1))
                                .addGap(150, 150, 150)
                                .addComponent(cmbCountries, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(lblCapital, javax.swing.GroupLayout.PREFERRED_SIZE, 264, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(65, 65, 65)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lblFlag, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnGetCountry, javax.swing.GroupLayout.DEFAULT_SIZE, 212, Short.MAX_VALUE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel1)
                .addGap(34, 34, 34)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbCountries, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnGetCountry)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 26, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblFlag, javax.swing.GroupLayout.DEFAULT_SIZE, 162, Short.MAX_VALUE)
                    .addComponent(lblCapital, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(35, 35, 35)
                .addComponent(lblLoaded, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        lblCapital.getAccessibleContext().setAccessibleName("lblCapital");
        cmbCountries.getAccessibleContext().setAccessibleName("cmbCountries");
        btnGetCountry.getAccessibleContext().setAccessibleName("btnGetCountry");
        lblFlag.getAccessibleContext().setAccessibleName("lblFlag");
        lblLoaded.getAccessibleContext().setAccessibleName("lblLoaded");

        btnExit.setText("Exit");
        btnExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExitActionPerformed(evt);
            }
        });

        btnAddToCompare.setText("Add To Compare");
        btnAddToCompare.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddToCompareActionPerformed(evt);
            }
        });

        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));

        jScrollPane2.setViewportView(lstCountries);
        lstCountries.getAccessibleContext().setAccessibleName("lstCountries");

        jScrollPane3.setViewportView(lstStatistics);
        lstStatistics.getAccessibleContext().setAccessibleName("lstStatistics");

        btnClearList.setText("Clear List");
        btnClearList.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearListActionPerformed(evt);
            }
        });

        btnShowStatistics.setText("Show Statistics");
        btnShowStatistics.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnShowStatisticsActionPerformed(evt);
            }
        });

        jPanel4.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, null, new java.awt.Color(204, 204, 204), new java.awt.Color(0, 0, 0), null));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 9, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 247, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnClearList, javax.swing.GroupLayout.DEFAULT_SIZE, 278, Short.MAX_VALUE)
                    .addComponent(jScrollPane2))
                .addGap(84, 84, 84)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(78, 78, 78)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane3)
                    .addComponent(btnShowStatistics, javax.swing.GroupLayout.DEFAULT_SIZE, 656, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnClearList)
                            .addComponent(btnShowStatistics))))
                .addContainerGap(88, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(20, 1078, Short.MAX_VALUE)
                        .addComponent(btnExit))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnAddToCompare, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnAddToCompare)
                .addGap(18, 18, 18)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(btnExit)
                .addGap(28, 28, 28))
        );

        btnExit.getAccessibleContext().setAccessibleName("btnExit");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    //method to fetch the detailed info for the selected country and display it in the label along with the flag
    private void btnGetCountryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGetCountryActionPerformed
        String selectedCountry = (String) cmbCountries.getSelectedItem();
        if (selectedCountry == null) {
            lblCapital.setText("Please select a country first.");
            return;
        }
        
        try {
            //Preventing SQL injection with PreparedStatement also it handcles special characters
            String sql = "SELECT Capital, Currency, Population, Continent, CountryCode FROM CountryCapital WHERE CountryName = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, selectedCountry);
            ResultSet rs = pstmt.executeQuery();
            //check if record was found and safely retrieve all fields, handling null and 0 error
            if (rs.next()) {
                String capital = getStringSafe(rs, "Capital", null);
                String currency = getStringSafe(rs, "Currency", "N/A");
                long population = getLongSafe(rs, "Population", -1); // -1 means missing
                String continent = getStringSafe(rs, "Continent", "N/A");
                String countryCode = getStringSafe(rs, "CountryCode", null);
                //string builder helps me arrange the text inside the SwingLabels ( it supports basic HTML )
                StringBuilder sb = new StringBuilder();
                sb.append("<html>");//start html doc
                sb.append("<b>").append(selectedCountry).append("</b><br><br>");
                
                if (capital == null) {
                    sb.append("Capital: <i>Not available</i><br>");
                } else {
                    sb.append("Capital: ").append(capital).append("<br>");
                }
                
                sb.append("Currency: ").append(currency).append("<br>");
                //formated population to display with commas 000,000,000 or show N/A not available
                String popText = (population == -1) ? "N/A" : String.format("%,d", population);
                sb.append("Population: ").append(popText).append("<br>");
                sb.append("Continent: ").append(continent);
                sb.append("</html>");//close html code
                
                lblCapital.setText(sb.toString());//update the UI label
                loadFlag(countryCode);//method to trigger the flag loader
            } else {
                lblCapital.setText("No data found for " + selectedCountry);
                lblFlag.setIcon(null);
                lblFlag.setText("No Flag");
            }
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            lblCapital.setText("Error retrieving data.");
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnGetCountryActionPerformed
//Takes the currently selected country and adds it to the comparison JList (lstCountries).
    private void btnAddToCompareActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddToCompareActionPerformed
        String selectedCountry = (String) cmbCountries.getSelectedItem();
        if (selectedCountry == null) {
            JOptionPane.showMessageDialog(this, "Please select a country first.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        // Prevent the same country from being added twice ( duplication )
        if (countriesListModel.contains(selectedCountry)) {
            JOptionPane.showMessageDialog(this, selectedCountry + " is already in the list.", "Duplicate", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        countriesListModel.addElement(selectedCountry);
    }//GEN-LAST:event_btnAddToCompareActionPerformed
//Empties both the comparison list and the statistics list
    private void btnClearListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearListActionPerformed
        countriesListModel.clear();
        statisticsListModel.clear();
    }//GEN-LAST:event_btnClearListActionPerformed
//Reads all countries in the comparison list, fetches their data from the database, and calculates poopulation
    private void btnShowStatisticsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnShowStatisticsActionPerformed
        statisticsListModel.clear();//clear previous results 
        //check if any countries are added to list
        if (countriesListModel.isEmpty()) {
            statisticsListModel.addElement("No countries added to compare.");
            return;
        }
        //variables for population comparison 
        String largestPopCountry = "";
        String smallestPopCountry = "";
        long maxPop = Long.MIN_VALUE;
        long minPop = Long.MAX_VALUE;
        int validPopCount = 0; // How many countries actually had population data
        long totalPop = 0; // Running total to calculate average later
        int totalCountries = countriesListModel.size();
        
        try {
            statisticsListModel.addElement("=== COUNTRY COMPARISON ===");
            statisticsListModel.addElement("");
             // Loop through every country that was added to list
            for (int i = 0; i < countriesListModel.size(); i++) {
                String country = countriesListModel.getElementAt(i);
                
                String sql = "SELECT Capital, Population FROM CountryCapital WHERE CountryName = ?";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, country);
                ResultSet rs = pstmt.executeQuery();
                
                if (rs.next()) {
                    String capital = getStringSafe(rs, "Capital", "N/A");
                    long population = getLongSafe(rs, "Population", -1);
                    // Add the countryes info to the statistics list
                    String popDisplay = (population == -1) ? "N/A" : String.format("%,d", population);
                    statisticsListModel.addElement(country + "  |  Capital: " + capital + "  |  Pop: " + popDisplay);
                    // If the population is valid, include it in the math calculations had issues where population was 0
                    if (population > 0) {
                        validPopCount++;
                        totalPop += population;
                        // Check if this is the new largest population
                        if (population > maxPop) { maxPop = population; largestPopCountry = country; }
                        // Check if this is the new smallest population
                        if (population < minPop) { minPop = population; smallestPopCountry = country; }
                    }
                } else {
                    statisticsListModel.addElement(country + "  |  NO DATA FOUND");
                }
                rs.close();
                pstmt.close();
            }
            //Print summary 
            statisticsListModel.addElement("");
            statisticsListModel.addElement("=== SUMMARY ===");
            statisticsListModel.addElement("Total compared: " + totalCountries);
            // Can only show difference and average if at least 2 countries have valid data
            if (validPopCount > 0) {
                statisticsListModel.addElement("Largest pop: " + largestPopCountry + " (" + String.format("%,d", maxPop) + ")");
                statisticsListModel.addElement("Smallest pop: " + smallestPopCountry + " (" + String.format("%,d", minPop) + ")");
                if (validPopCount > 1) {
                    statisticsListModel.addElement("Pop difference: " + String.format("%,d", maxPop - minPop));
                    statisticsListModel.addElement("Average pop: " + String.format("%,d", totalPop / validPopCount));
                }
            } else {
                statisticsListModel.addElement("No valid population data available.");
            }
        } catch (SQLException e) {
            statisticsListModel.addElement("Error comparing: " + e.getMessage());
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnShowStatisticsActionPerformed

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CountriesCapitals.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        java.awt.EventQueue.invokeLater(() -> { new CountriesCapitals().setVisible(true); });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddToCompare;
    private javax.swing.JButton btnClearList;
    private javax.swing.JButton btnExit;
    private javax.swing.JButton btnGetCountry;
    private javax.swing.JButton btnShowStatistics;
    private javax.swing.JComboBox<String> cmbCountries;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel lblCapital;
    private javax.swing.JLabel lblFlag;
    private javax.swing.JLabel lblLoaded;
    private javax.swing.JList<String> lstCountries;
    private javax.swing.JList<String> lstStatistics;
    // End of variables declaration//GEN-END:variables
}