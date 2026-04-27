package com.cinemaebooking.backend.bootstrap.seed;

import com.cinemaebooking.backend.bootstrap.reset.ResetService;
import com.cinemaebooking.backend.cinema.domain.model.Cinema;
import com.cinemaebooking.backend.user.application.port.UserRepository;
import com.cinemaebooking.backend.user.domain.enums.UserRole;
import com.cinemaebooking.backend.user.domain.enums.UserStatus;
import com.cinemaebooking.backend.user.domain.model.User;
import com.cinemaebooking.backend.user.application.port.PasswordEncoder;
import com.cinemaebooking.backend.user.domain.valueObject.UserGender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;


@Service
@RequiredArgsConstructor
public class SeedServiceImpl implements SeedService {
   // private final ResetService resetService;
    private final CinemaSeed cinemaSeed;
    private final RoomSeed roomSeed;
    @Override
    public void seed() {

    }
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Override
    public void seedAdmin() {
        String adminEmail = "admin@gmail.com";

        boolean exists = userRepository.existsByEmail(adminEmail);
        if (exists) return;

        User admin = User.builder()
                .fullName("Admin")
                .email(adminEmail)
                .password(passwordEncoder.encode("123456"))
                .phoneNumber("0708378286")
                .dateOfBirth(LocalDate.of(2005,12,2))
                .gender(UserGender.MALE)
                .avatarUrl(null)
                .role(UserRole.ADMIN)
                .status(UserStatus.ACTIVE)
                .build();

        userRepository.create(admin);

        System.out.println("🔥 Admin account created: admin@gmail.com / 123456");
    }
}
