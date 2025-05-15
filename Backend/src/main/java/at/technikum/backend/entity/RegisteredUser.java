package at.technikum.backend.entity;

public class RegisteredUser {

    private char sex;   //char: w,m,d keine anderen werden akzeptiert
    private String username;
    private String email;
    private String password;
    private String land;
    private boolean isAdmin;

    public RegisteredUser(char sex, String username, String email, String password, String land){
        this.sex = sex;
        this.username = username;
        this.email = email;
        this.password = password;
        this.land = land;

        isAdmin = false; //dachte das Admin default mässig auf false ist, und nur von einem anderen Admin zu admin verändert werden kann
    }


    //GETTERS AND SETTERS
    public char getSex(){
        return this.sex;
    }
    public void setSex(char sex){
        this.sex = sex;
    }

    public String getUsername(){
        return this.username;
    }
    public void setUsername(){
        this.username = username;
    }

    public String getEmail(){
        return this.email;
    }
    public void setEmail(){
        this.email = email;
    }

    public String getPassword(){
        return this.password;
    }
    public void setPassword(){
        this.password = password;
    }

    public String getLand(){
        return this.land;
    }
    public void setLand(){
        this.land = land;
    }

    public boolean isAdmin(){
        return this.isAdmin;
    }
    public void setAdmin(boolean isAdmin){
        this.isAdmin = isAdmin;
    }



}