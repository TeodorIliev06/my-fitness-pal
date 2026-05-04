package com.fmi.myfitnesspal.persistence.user;

import com.fmi.myfitnesspal.persistence.PersistenceMapper;
import com.fmi.myfitnesspal.user.User;
import com.fmi.myfitnesspal.user.UserId;
import com.fmi.myfitnesspal.user.UserProfile;

public final class UserProfileDtoMapper implements PersistenceMapper<UserProfile, UserProfileDto> {
    @Override
    public UserProfileDto toDto(UserProfile userProfile) {
        return new UserProfileDto(
                userProfile.userId(),
                userProfile.userData()
        );
    }

    @Override
    public UserProfile toEntity(UserProfileDto userProfileDto) {
        UserId id = new UserId(userProfileDto.userId().username());
        User user = new User(
                userProfileDto.userData().height(),
                userProfileDto.userData().weight(),
                userProfileDto.userData().age(),
                userProfileDto.userData().sex(),
                userProfileDto.userData().country());

        return new UserProfile(id, user);
    }
}
