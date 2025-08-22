package org.gladyshev.ru.authservice1.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.gladyshev.ru.authservice1.DTO.UserRegistrationDTO;
import org.gladyshev.ru.authservice1.Service.AuthService;
import org.gladyshev.ru.authservice1.Entity.AuthUser;
import org.gladyshev.ru.authservice1.Service.RoleService;
import org.keycloak.admin.client.resource.RolesResource;
import org.keycloak.admin.client.resource.UserResource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final RoleService roleService;



    @PostMapping("/registration")
    public ResponseEntity<?> register(@RequestBody @Valid UserRegistrationDTO userRegistrationDTO) {

        authService.registration(userRegistrationDTO);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{id}/verification-email")
    public ResponseEntity<?> sendVerificationEmail(@PathVariable("id") String id) {

        authService.sendVerificationEmail(id);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/{id}/delete")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> deleteUser(@PathVariable("id") String id) {
        authService.deleteUser(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestParam String email) {
        authService.forgotPassword(email);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping("/roles/{id}")
    public ResponseEntity<?> assignRole(@PathVariable("id") String id, @RequestParam String roleName) {

        roleService.assignRole(id , roleName);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }

    @GetMapping("/info")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> info() {
        return ResponseEntity.ok("some info for user");
    }



}
