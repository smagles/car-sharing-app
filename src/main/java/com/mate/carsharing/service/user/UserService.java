package com.mate.carsharing.service.user;

import com.mate.carsharing.dto.user.UserDto;
import com.mate.carsharing.dto.user.UserRegistrationRequestDto;
import com.mate.carsharing.dto.user.UserUpdateRequestDto;

public interface UserService {
    /****
 * Registers a new user with the provided registration details.
 *
 * @param userDto the registration data for the new user
 * @return the created user's information
 */
UserDto register(UserRegistrationRequestDto userDto);

    /****
 * Retrieves user information for the specified email address.
 *
 * @param email the email address of the user whose information is to be retrieved
 * @return a UserDto containing the user's information
 */
UserDto getUserInfo(String email);

    /****
 * Updates the information of the user identified by the given email with the provided update data.
 *
 * @param email the email address of the user to update
 * @param request the data containing updated user information
 * @return the updated user information as a UserDto
 */
UserDto updateUserInfo(String email, UserUpdateRequestDto request);

    /****
 * Updates the role of the user identified by the given user ID.
 *
 * @param userId the unique identifier of the user whose role is to be changed
 * @param newRole the new role to assign to the user
 * @return the updated user information as a UserDto
 */
UserDto updateUserRole(Long userId, String newRole);
}
