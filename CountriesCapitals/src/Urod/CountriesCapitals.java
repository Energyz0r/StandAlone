
package Urod;

//imports 
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;

public class CountriesCapitals extends javax.swing.JFrame {

    // ===== DYNAMIC BASE PATH =====
    private static final String BASE_PATH = System.getProperty("user.dir");

    // ===== DATABASE CONNECTION =====
    private static final String DB_PATH = BASE_PATH + "/Data/Countries.accdb";
    private static final String DB_URL = "jdbc:ucanaccess://" + DB_PATH;

    // ===== IMAGES PATH =====
    private static final String IMG_PATH = BASE_PATH + "/images/";
    private Connection conn;
    private final DefaultListModel<String> countriesListModel = new DefaultListModel<>();
    private final DefaultListModel<String> statisticsListModel = new DefaultListModel<>();

    public CountriesCapitals() {
        initComponents();

        // ===== SET FRAME ICON =====
        java.io.File iconFile = new java.io.File(IMG_PATH + "icon.png");
        if (iconFile.exists()) {
            Image icon = new ImageIcon(iconFile.getAbsolutePath()).getImage();
            this.setIconImage(icon);
        }

        // Set up list models
        lstCountries.setModel(countriesListModel);
        lstStatistics.setModel(statisticsListModel);

        // Center images within their labels when label is bigger than icon
        lblFlag.setHorizontalAlignment(SwingConstants.CENTER);
        lblFlag.setVerticalAlignment(SwingConstants.CENTER);
        lblMap.setHorizontalAlignment(SwingConstants.CENTER);
        lblMap.setVerticalAlignment(SwingConstants.CENTER);

        lblLoaded.setText("Connecting to database...");
        connectToDatabase();
    }

    // ===== DATABASE METHODS =====
    private void connectToDatabase() {
        try {
            conn = DriverManager.getConnection(DB_URL);
            lblLoaded.setText("Database connected successfully!");
            loadCountries();
        } catch (SQLException e) {
            lblLoaded.setText("Database connection FAILED!");
            JOptionPane.showMessageDialog(this,
                "Could not connect to database:\n" + e.getMessage(),
                "Connection Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void loadCountries() {
        cmbCountries.removeAllItems();
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT CountryName FROM CountryCapital ORDER BY CountryName");
            while (rs.next()) {
                String country = rs.getString("CountryName");
                if (country != null && !country.trim().isEmpty()) {
                    cmbCountries.addItem(country.trim());
                }
            }
            rs.close();
            stmt.close();
            lblLoaded.setText("Loaded " + cmbCountries.getItemCount() + " countries.");
        } catch (SQLException e) {
            lblLoaded.setText("Error loading countries.");
            e.printStackTrace();
        }
    }

    private String getStringSafe(ResultSet rs, String columnName, String defaultValue) {
        try {
            String value = rs.getString(columnName);
            if (value == null || value.trim().isEmpty()) {
                return defaultValue;
            }
            return value.trim();
        } catch (SQLException e) {
            return defaultValue;
        }
    }

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

    // ===== FLAG LOADING METHOD (WITH ASPECT RATIO PRESERVED) =====
    private void loadFlag(String countryCode) {
        if (countryCode == null || countryCode.trim().isEmpty()) {
            lblFlag.setIcon(null);
            lblFlag.setText("No Flag");
            return;
        }

        String filePath = IMG_PATH + countryCode.toLowerCase() + ".png";
        File file = new File(filePath);
        if (!file.exists()) {
            lblFlag.setIcon(null);
            lblFlag.setText("Flag missing");
            return;
        }

        try {
            ImageIcon icon = new ImageIcon(filePath);
            int w = Math.max(lblFlag.getWidth(), 50);
            int h = Math.max(lblFlag.getHeight(), 50);
            
            // Preserve aspect ratio
            int origW = icon.getIconWidth();
            int origH = icon.getIconHeight();
            double scale = Math.min((double)w / origW, (double)h / origH);
            int scaledW = (int) (origW * scale);
            int scaledH = (int) (origH * scale);
            
            Image img = icon.getImage().getScaledInstance(scaledW, scaledH, Image.SCALE_SMOOTH);
            lblFlag.setIcon(new ImageIcon(img));
            lblFlag.setText("");
        } catch (Exception e) {
            lblFlag.setIcon(null);
            lblFlag.setText("Error loading flag");
            e.printStackTrace();
        }
    }

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
        lblMap = new javax.swing.JLabel();
        btnExit = new javax.swing.JButton();
        btnAddToCompare = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        lstCountries = new javax.swing.JList<>();
        jScrollPane3 = new javax.swing.JScrollPane();
        lstStatistics = new javax.swing.JList<>();
        btnClearList = new javax.swing.JButton();
        btnShowStatistics = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Countries And Their Capitals");

        jPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel1.setForeground(new java.awt.Color(153, 153, 153));

        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 16));
        jLabel1.setText("Countries And Their Capitals");

        jLabel2.setText("Select a country :");

        lblCapital.setText("Select a country and I will display its capital.");
        lblCapital.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        btnGetCountry.setText("Get Country");
        btnGetCountry.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGetCountryActionPerformed(evt);
            }
        });

        lblFlag.setText("Flag");
        lblFlag.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        lblLoaded.setText("jLabel5");

        lblMap.setText("Map");
        lblMap.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        // ===== jPanel2 LAYOUT (TOP PANEL - FIXED HEIGHT) =====
        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbCountries, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnGetCountry))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(lblCapital, 0, 250, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblFlag, 180, 180, 180) // Fixed width horizontally
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblMap, 0, 400, Short.MAX_VALUE)) // Grows horizontally
                    .addComponent(lblLoaded, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                )
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(cmbCountries, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnGetCountry))
                .addGap(18, 18, 18)
                // STRICT 162px HEIGHT FOR ALL THREE LABELS
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblCapital, 0, 162, 162)
                    .addComponent(lblFlag, 0, 162, 162)
                    .addComponent(lblMap, 0, 162, 162))
                .addGap(18, 18, 18)
                .addComponent(lblLoaded, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        lblCapital.getAccessibleContext().setAccessibleName("lblCapital");
        cmbCountries.getAccessibleContext().setAccessibleName("cmbCountries");
        btnGetCountry.getAccessibleContext().setAccessibleName("btnGetCountry");
        lblFlag.getAccessibleContext().setAccessibleName("lblFlag");
        lblLoaded.getAccessibleContext().setAccessibleName("lblLoaded");
        lblMap.getAccessibleContext().setAccessibleName("lblMap");

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

        // ===== jPanel3 LAYOUT (BOTTOM PANEL - STRETCHES) =====
        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, 0, 300, Short.MAX_VALUE)
                    .addComponent(btnClearList, 0, 300, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, 0, 500, Short.MAX_VALUE)
                    .addComponent(btnShowStatistics, 0, 500, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, 0, 205, Short.MAX_VALUE)
                    .addComponent(jScrollPane3, 0, 205, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnClearList)
                    .addComponent(btnShowStatistics))
                .addContainerGap())
        );

        // ===== jPanel1 LAYOUT (ROOT PANEL) =====
        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnExit))
                    .addComponent(btnAddToCompare, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE) // TOP FIXED
                .addGap(18, 18, 18)
                .addComponent(btnAddToCompare)
                .addGap(18, 18, 18)
                .addComponent(jPanel3, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE) // BOTTOM STRETCHES
                .addGap(18, 18, 18)
                .addComponent(btnExit)
                .addContainerGap())
        );

        btnExit.getAccessibleContext().setAccessibleName("btnExit");

        // ===== FRAME LAYOUT =====
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnGetCountryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGetCountryActionPerformed
        String selectedCountry = (String) cmbCountries.getSelectedItem();
        if (selectedCountry == null) {
            lblCapital.setText("Please select a country first.");
            return;
        }

        try {
            String sql = "SELECT Capital, Currency, Population, Continent, CountryCode FROM CountryCapital WHERE CountryName = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, selectedCountry);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String capital = getStringSafe(rs, "Capital", null);
                String currency = getStringSafe(rs, "Currency", "N/A");
                long population = getLongSafe(rs, "Population", -1);
                String continent = getStringSafe(rs, "Continent", "N/A");
                String countryCode = getStringSafe(rs, "CountryCode", null);

                StringBuilder sb = new StringBuilder();
                sb.append("<html>");
                sb.append("<b>").append(selectedCountry).append("</b><br><br>");

                if (capital == null) {
                    sb.append("Capital: <i>Not available</i><br>");
                } else {
                    sb.append("Capital: ").append(capital).append("<br>");
                }

                sb.append("Currency: ").append(currency).append("<br>");
                String popText = (population == -1) ? "N/A" : String.format("%,d", population);
                sb.append("Population: ").append(popText).append("<br>");
                sb.append("Continent: ").append(continent);
                sb.append("</html>");

                lblCapital.setText(sb.toString());
                loadFlag(countryCode);
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

        loadMapFromWeb(selectedCountry);
    }//GEN-LAST:event_btnGetCountryActionPerformed

    private void btnAddToCompareActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddToCompareActionPerformed
        String selectedCountry = (String) cmbCountries.getSelectedItem();
        if (selectedCountry == null) {
            JOptionPane.showMessageDialog(this, "Please select a country first.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (countriesListModel.contains(selectedCountry)) {
            JOptionPane.showMessageDialog(this, selectedCountry + " is already in the list.", "Duplicate", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        countriesListModel.addElement(selectedCountry);
    }//GEN-LAST:event_btnAddToCompareActionPerformed

    private void btnClearListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearListActionPerformed
        countriesListModel.clear();
        statisticsListModel.clear();
    }//GEN-LAST:event_btnClearListActionPerformed

    private void btnShowStatisticsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnShowStatisticsActionPerformed
        statisticsListModel.clear();
        if (countriesListModel.isEmpty()) {
            statisticsListModel.addElement("No countries added to compare.");
            return;
        }

        String largestPopCountry = "";
        String smallestPopCountry = "";
        long maxPop = Long.MIN_VALUE;
        long minPop = Long.MAX_VALUE;
        int validPopCount = 0;
        long totalPop = 0;
        int totalCountries = countriesListModel.size();

        try {
            statisticsListModel.addElement("=== COUNTRY COMPARISON ===");
            statisticsListModel.addElement("");

            for (int i = 0; i < countriesListModel.size(); i++) {
                String country = countriesListModel.getElementAt(i);

                String sql = "SELECT Capital, Population FROM CountryCapital WHERE CountryName = ?";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, country);
                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {
                    String capital = getStringSafe(rs, "Capital", "N/A");
                    long population = getLongSafe(rs, "Population", -1);
                    String popDisplay = (population == -1) ? "N/A" : String.format("%,d", population);
                    statisticsListModel.addElement(country + "  |  Capital: " + capital + "  |  Pop: " + popDisplay);

                    if (population > 0) {
                        validPopCount++;
                        totalPop += population;
                        if (population > maxPop) { maxPop = population; largestPopCountry = country; }
                        if (population < minPop) { minPop = population; smallestPopCountry = country; }
                    }
                } else {
                    statisticsListModel.addElement(country + "  |  NO DATA FOUND");
                }
                rs.close();
                pstmt.close();
            }

            statisticsListModel.addElement("");
            statisticsListModel.addElement("=== SUMMARY ===");
            statisticsListModel.addElement("Total compared: " + totalCountries);

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

    // ===== MAP LOADING METHOD (WITH ASPECT RATIO PRESERVED) =====
    private void loadMapFromWeb(String countryName) {
        lblMap.setIcon(null);
        lblMap.setText("Loading map...");

        new javax.swing.SwingWorker<ImageIcon, Void>() {
            @Override
            protected ImageIcon doInBackground() throws Exception {
                // === STEP 1: Geocode country name → lat, lon ===
                String geocodeUrl = "https://nominatim.openstreetmap.org/search?q="
                    + URLEncoder.encode(countryName, "UTF-8")
                    + "&format=json&limit=1";

                java.net.HttpURLConnection geoConn =
                    (java.net.HttpURLConnection) new java.net.URL(geocodeUrl).openConnection();
                geoConn.setRequestProperty("User-Agent", "CountriesCapitalsApp/1.0");
                geoConn.setConnectTimeout(8000);
                geoConn.setReadTimeout(8000);

                BufferedReader reader = new BufferedReader(
                    new InputStreamReader(geoConn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) sb.append(line);
                reader.close();
                geoConn.disconnect();

                String json = sb.toString();
                if (json.equals("[]")) throw new Exception("Country not found by geocoder");

                String latStr = extractJsonString(json, "lat");
                String lonStr = extractJsonString(json, "lon");
                if (latStr == null || lonStr == null) throw new Exception("No coordinates in response");

                double lat = Double.parseDouble(latStr);
                double lon = Double.parseDouble(lonStr);

                // === STEP 2: Calculate which OSM tile the center falls on ===
                int zoom = 5;
                int centerTileX = (int) Math.floor((lon + 180.0) / 360.0 * Math.pow(2.0, zoom));
                double latRad = Math.toRadians(lat);
                int centerTileY = (int) Math.floor(
                    (1.0 - Math.log(Math.tan(latRad) + 1.0 / Math.cos(latRad)) / Math.PI)
                    / 2.0 * Math.pow(2.0, zoom));

                // === STEP 3: Fetch a 3×3 grid of 256×256 tiles and stitch them ===
                int gridSize = 3;
                int tileSize = 256;
                int startTileX = centerTileX - gridSize / 2;
                int startTileY = centerTileY - gridSize / 2;

                BufferedImage mapImage = new BufferedImage(
                    gridSize * tileSize, gridSize * tileSize, BufferedImage.TYPE_INT_RGB);
                Graphics g = mapImage.getGraphics();

                g.setColor(java.awt.Color.LIGHT_GRAY);
                g.fillRect(0, 0, mapImage.getWidth(), mapImage.getHeight());

                for (int tx = 0; tx < gridSize; tx++) {
                    for (int ty = 0; ty < gridSize; ty++) {
                        try {
                            String tileUrl = "https://tile.openstreetmap.org/"
                                + zoom + "/" + (startTileX + tx) + "/" + (startTileY + ty) + ".png";

                            java.net.HttpURLConnection tileConn =
                                (java.net.HttpURLConnection) new java.net.URL(tileUrl).openConnection();
                            tileConn.setRequestProperty("User-Agent", "CountriesCapitalsApp/1.0");
                            tileConn.setConnectTimeout(5000);
                            tileConn.setReadTimeout(5000);

                            BufferedImage tile = javax.imageio.ImageIO.read(tileConn.getInputStream());
                            tileConn.disconnect();

                            if (tile != null) {
                                g.drawImage(tile, tx * tileSize, ty * tileSize, null);
                            }
                        } catch (Exception tileEx) {
                            // Skip failed tiles
                        }
                        Thread.sleep(300);
                    }
                }
                g.dispose();

                // === STEP 4: Draw a red marker at the center ===
                int markerX = (gridSize / 2) * tileSize + tileSize / 2;
                int markerY = (gridSize / 2) * tileSize + tileSize / 2;
                Graphics mg = mapImage.getGraphics();
                mg.setColor(java.awt.Color.RED);
                mg.fillOval(markerX - 8, markerY - 8, 16, 16);
                mg.setColor(java.awt.Color.WHITE);
                mg.fillOval(markerX - 4, markerY - 4, 8, 8);
                mg.dispose();

                return new ImageIcon(mapImage);
            }

            @Override
            protected void done() {
                try {
                    ImageIcon icon = get();
                    int w = Math.max(lblMap.getWidth(), 200);
                    int h = Math.max(lblMap.getHeight(), 200);
                    
                    // Preserve aspect ratio so it doesn't stretch/distort
                    int origW = icon.getIconWidth();
                    int origH = icon.getIconHeight();
                    double scale = Math.min((double)w / origW, (double)h / origH);
                    int scaledW = (int) (origW * scale);
                    int scaledH = (int) (origH * scale);
                    
                    Image img = icon.getImage().getScaledInstance(scaledW, scaledH, Image.SCALE_SMOOTH);
                    lblMap.setIcon(new ImageIcon(img));
                    lblMap.setText("");
                } catch (Exception e) {
                    lblMap.setIcon(null);
                    lblMap.setText("Map failed to load");
                    e.printStackTrace();
                }
            }
        }.execute();
    }

    /** Simple JSON string extractor */
    private String extractJsonString(String json, String key) {
        String searchKey = "\"" + key + "\":\"";
        int start = json.indexOf(searchKey);
        if (start == -1) return null;
        start += searchKey.length();
        int end = json.indexOf("\"", start);
        if (end == -1) return null;
        return json.substring(start, end);
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
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel lblCapital;
    private javax.swing.JLabel lblFlag;
    private javax.swing.JLabel lblLoaded;
    private javax.swing.JLabel lblMap;
    private javax.swing.JList<String> lstCountries;
    private javax.swing.JList<String> lstStatistics;
    // End of variables declaration//GEN-END:variables
}
