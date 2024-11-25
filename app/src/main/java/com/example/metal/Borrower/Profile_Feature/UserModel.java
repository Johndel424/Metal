package com.example.metal.Borrower.Profile_Feature;

public class UserModel {
    private String Age;
    private String Brgy;
    private String City;
    private String ImgInternetBill;
    private String ImgMeralcoBill;
    private String ImgPermit;
    private String ImgValidID;
    private String ImgWaterBill;
    private String InternetBill;
    private String MaritalStatus;
    private String MeralcoBill;
    private String MonthlyIncome;
    private String Name;
    private String Padala;
    private String ProfileImage;
    private String Province;
    private String Street;
    private String WaterBill;
    private String Work;
    private String accountDetailsComplete;
    private String accountDocumentsComplete;
    private String accountStatus;
    private String lineOfWork;
    private String phoneNumber;
    private Double totalMonthlyBill;
    private Double totalIncomeMonthly;
    private String uid;
    private String usertype;
    private int creditScore;
    private int currentLoan;
    private long timestamp;
    double previousLoanBalance;

    // Constructor
    public UserModel(String Age, String Brgy, String City, String ImgInternetBill, String ImgMeralcoBill,
                     String ImgPermit, String ImgValidID, String ImgWaterBill, String InternetBill,
                     String MaritalStatus, String MeralcoBill, String MonthlyIncome, String Name,
                     String Padala, String ProfileImage, String Province, String Street, String WaterBill,
                     String Work, String accountDetailsComplete, String accountDocumentsComplete,
                     String accountStatus, String lineOfWork, String phoneNumber, Double totalMonthlyBill,
                     Double totalIncomeMonthly, String uid, String userType, int creditScore, int currentLoan,long timestamp) {
        this.Age = Age;
        this.Brgy = Brgy;
        this.City = City;
        this.ImgInternetBill = ImgInternetBill;
        this.ImgMeralcoBill = ImgMeralcoBill;
        this.ImgPermit = ImgPermit;
        this.ImgValidID = ImgValidID;
        this.ImgWaterBill = ImgWaterBill;
        this.InternetBill = InternetBill;
        this.MaritalStatus = MaritalStatus;
        this.MeralcoBill = MeralcoBill;
        this.MonthlyIncome = MonthlyIncome;
        this.Name = Name;
        this.Padala = Padala;
        this.ProfileImage = ProfileImage;
        this.Province = Province;
        this.Street = Street;
        this.WaterBill = WaterBill;
        this.Work = Work;
        this.accountDetailsComplete = accountDetailsComplete;
        this.accountDocumentsComplete = accountDocumentsComplete;
        this.accountStatus = accountStatus;
        this.lineOfWork = lineOfWork;
        this.phoneNumber = phoneNumber;
        this.totalMonthlyBill = totalMonthlyBill;
        this.totalIncomeMonthly = totalIncomeMonthly;
        this.uid = uid;
        this.usertype = userType;
        this.creditScore = creditScore;
        this.currentLoan = currentLoan;
        this.timestamp = timestamp;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getAge() {
        return Age;
    }

    public double getPreviousLoanBalance() {
        return previousLoanBalance;
    }

    public void setPreviousLoanBalance(double previousLoanBalance) {
        this.previousLoanBalance = previousLoanBalance;
    }

    public void setAge(String age) {
        Age = age;
    }

    public String getBrgy() {
        return Brgy;
    }

    public void setBrgy(String brgy) {
        Brgy = brgy;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getImgInternetBill() {
        return ImgInternetBill;
    }

    public void setImgInternetBill(String imgInternetBill) {
        ImgInternetBill = imgInternetBill;
    }

    public String getImgMeralcoBill() {
        return ImgMeralcoBill;
    }

    public void setImgMeralcoBill(String imgMeralcoBill) {
        ImgMeralcoBill = imgMeralcoBill;
    }

    public String getImgPermit() {
        return ImgPermit;
    }

    public void setImgPermit(String imgPermit) {
        ImgPermit = imgPermit;
    }

    public String getImgValidID() {
        return ImgValidID;
    }

    public void setImgValidID(String imgValidID) {
        ImgValidID = imgValidID;
    }

    public String getImgWaterBill() {
        return ImgWaterBill;
    }

    public void setImgWaterBill(String imgWaterBill) {
        ImgWaterBill = imgWaterBill;
    }

    public String getInternetBill() {
        return InternetBill;
    }

    public void setInternetBill(String internetBill) {
        InternetBill = internetBill;
    }

    public String getMaritalStatus() {
        return MaritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        MaritalStatus = maritalStatus;
    }

    public String getMeralcoBill() {
        return MeralcoBill;
    }

    public void setMeralcoBill(String meralcoBill) {
        MeralcoBill = meralcoBill;
    }

    public String getMonthlyIncome() {
        return MonthlyIncome;
    }

    public void setMonthlyIncome(String monthlyIncome) {
        MonthlyIncome = monthlyIncome;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPadala() {
        return Padala;
    }

    public void setPadala(String padala) {
        Padala = padala;
    }

    public String getProfileImage() {
        return ProfileImage;
    }

    public void setProfileImage(String profileImage) {
        ProfileImage = profileImage;
    }

    public String getProvince() {
        return Province;
    }

    public void setProvince(String province) {
        Province = province;
    }

    public String getStreet() {
        return Street;
    }

    public void setStreet(String street) {
        Street = street;
    }

    public String getWaterBill() {
        return WaterBill;
    }

    public void setWaterBill(String waterBill) {
        WaterBill = waterBill;
    }

    public String getWork() {
        return Work;
    }

    public void setWork(String work) {
        Work = work;
    }

    public String getAccountDetailsComplete() {
        return accountDetailsComplete;
    }

    public void setAccountDetailsComplete(String accountDetailsComplete) {
        this.accountDetailsComplete = accountDetailsComplete;
    }

    public String getAccountDocumentsComplete() {
        return accountDocumentsComplete;
    }

    public void setAccountDocumentsComplete(String accountDocumentsComplete) {
        this.accountDocumentsComplete = accountDocumentsComplete;
    }

    public String getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }

    public String getLineOfWork() {
        return lineOfWork;
    }

    public void setLineOfWork(String lineOfWork) {
        this.lineOfWork = lineOfWork;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Double getTotalMonthlyBill() {
        return totalMonthlyBill;
    }

    public void setTotalMonthlyBill(Double totalMonthlyBill) {
        this.totalMonthlyBill = totalMonthlyBill;
    }

    public Double getTotalIncomeMonthly() {
        return totalIncomeMonthly;
    }

    public void setTotalIncomeMonthly(Double totalIncomeMonthly) {
        this.totalIncomeMonthly = totalIncomeMonthly;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUserType() {
        return usertype;
    }

    public void setUserType(String userType) {
        this.usertype = userType;
    }

    public int getCreditScore() {
        return creditScore;
    }

    public void setCreditScore(int creditScore) {
        this.creditScore = creditScore;
    }

    public int getCurrentLoan() {
        return currentLoan;
    }

    public void setCurrentLoan(int currentLoan) {
        this.currentLoan = currentLoan;
    }

    // Required empty constructor (no-argument constructor)
    public UserModel() {
        // Default constructor required for calls to DataSnapshot.getValue(UserModel.class)
    }
}
