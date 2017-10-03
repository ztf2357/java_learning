package com.beyondhost.week4;

import lombok.Data;
import lombok.NonNull;
import lombok.ToString;

@Data
@ToString(exclude = {"Email","Name","Gender","Mobile"})
public class User {
    @NonNull
    public long Id;
    public String Name;
    public boolean Gender;
    public String Mobile;
    public String Email;
}
