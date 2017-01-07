package agrechnev.web;

import agrechnev.sampledb.SampleDB;
import agrechnev.security.ExtraAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * Controller for "Create Sample DB" and "Delete Everything" admin opearations
 * Created by Oleksiy Grechnyev on 1/7/2017.
 */
@RestController
@RequestMapping("/rest/sample")
public class SampleController {

    // Beans

    @Autowired
    private ExtraAuthService extraAuthService; // Some extra authentication operations

    @Autowired
    private SampleDB sampleDB;

    /**
     * Create sample DB
     *
     * @param passw     Admin password to confirm
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/create")
    public ResponseEntity<?> create(@RequestBody String passw, Principal principal) {

        // Spring Security should do the admin check, but let's play it safe
        if (extraAuthService.isAdmin(principal) &&
                extraAuthService.authenticates(extraAuthService.getId(principal), passw)) {

            sampleDB.createSampleDB(); // The actual opeartion

            return ResponseEntity.ok(null);

        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
    }

    /**
     * Delete everything
     *
     * @param passw     Admin password to confirm
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/delete")
    public ResponseEntity<?> delete(@RequestBody String passw, Principal principal) {

        // Spring Security should do the admin check, but let's play it safe
        if (extraAuthService.isAdmin(principal) &&
                extraAuthService.authenticates(extraAuthService.getId(principal), passw)) {

            sampleDB.deleteEverything(); // The actual opeartion

            return ResponseEntity.ok(null);

        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
    }
}
