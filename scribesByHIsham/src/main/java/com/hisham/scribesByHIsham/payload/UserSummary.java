package com.hisham.scribesByHIsham.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserSummary {
    private Long id;

    private String username;

    private String firstname;

    private String lastname;
}
