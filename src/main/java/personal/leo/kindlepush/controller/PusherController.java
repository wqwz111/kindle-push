package personal.leo.kindlepush.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.concurrent.FailureCallback;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.SuccessCallback;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.multipart.MultipartFile;
import personal.leo.kindlepush.error.StorageException;
import personal.leo.kindlepush.service.FileService;
import personal.leo.kindlepush.service.MailService;

import javax.mail.MessagingException;
import java.io.IOException;
import java.nio.file.Path;

@RestController
@RequestMapping(path = "/push")
public class PusherController {

    private final FileService fileService;
    private final MailService mailService;

    @Autowired
    public PusherController(FileService fileService, MailService mailService) {
        this.fileService = fileService;
        this.mailService = mailService;
    }

    @PostMapping(path = "/{userId}")
    public DeferredResult<ResponseEntity<?>> pushBook(@PathVariable("userId") String userId,
                                   @RequestParam("file") MultipartFile file) throws StorageException {

        DeferredResult<ResponseEntity<?>> deferredResult = new DeferredResult<>();
        ListenableFuture<Path> path = fileService.save(userId, file);
        path.addCallback(result -> {
            try {
                mailService.prepare(userId).sendMail(result);
            } catch (MessagingException e) {
                deferredResult.setErrorResult(e);
                return;
            }
            deferredResult.setResult(new ResponseEntity<>("success", HttpStatus.OK));
        }, deferredResult::setErrorResult);

        return deferredResult;
    }

    @GetMapping(path = "/status/{userId}/{id}")
    public String getStatus(@PathVariable("userId") String userId,
                          @PathVariable("id") String id) {
        return "" + userId + " ----- " + id;
    }

    @GetMapping(path = "/allstatus/{userId}")
    public String getAllStatus(@PathVariable("userId") String userId) {
        return "";
    }

}
