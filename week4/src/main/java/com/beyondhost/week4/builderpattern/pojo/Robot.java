package com.beyondhost.week4.builderpattern.pojo;

import lombok.Builder;
import lombok.Data;

@Data
public class Robot {
    public String head;
    public String body;
    public String feet;

    public static Builder builder(){
        return new Builder();
    }

    public static class Builder
    {
        private String _head;
        private String _body;
        private String _feet;

        public Builder buildHead(String head)
        {
            this._head = head;
            return this;
        }

        public Builder buildBody(String body)
        {
            this._body = body;
            return this;
        }

        public Builder buildFeet(String feet)
        {
            this._feet = feet;
            return this;
        }

        public Robot build()
        {
            Robot r = new Robot();
            r.setHead(_head);
            r.setBody(_body);
            r.setFeet(_feet);
            return r;
        }
    }
}

