package com.parlonsdev.resources;

import com.parlonsdev.dto.TrainingDto;
import com.parlonsdev.entities.Training;
import com.parlonsdev.message.Message;
import com.parlonsdev.repository.TrainingRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

@RestController
@CrossOrigin(origins = "*")
public class TrainingResource {

    private final TrainingRepository trainingRepository;

    public TrainingResource(TrainingRepository trainingRepository) {
        this.trainingRepository = trainingRepository;
    }
    
    private Sort.Direction getSortDirection(String direction) {
        if (direction.equals("asc")) {
          return Sort.Direction.ASC;
        } else if (direction.equals("desc")) {
          return Sort.Direction.DESC;
        }

        return Sort.Direction.ASC;
    }

    /*
     * PAGINATION AND SORTING :
     * URL : http://127.0.0.1:8080/trainings?page=index&size=number
     * URL : http://127.0.0.1:8080/trainings?page=index&size=number&sort=registered,desc&sort=name,asc
     * URL : http://127.0.0.1:8080/trainings?sort=name,asc
     * URL : http://127.0.0.1:8080/trainings?sort=registered,desc&sort=name,asc
     */
    @GetMapping("/sortedtrainings")
    public ResponseEntity<?> getAllTutorials(@RequestParam(defaultValue = "id,desc") String[] sort) {

      try {
        List<Order> orders = new ArrayList<Order>();

        if (sort[0].contains(",")) {
          for (String sortOrder : sort) {
            String[] _sort = sortOrder.split(",");
            orders.add(new Order(getSortDirection(_sort[1]), _sort[0]));
          }
        } else {
          // sort=[field, direction]
          orders.add(new Order(getSortDirection(sort[1]), sort[0]));
        }

        List<Training> trainings = trainingRepository.findAll(Sort.by(orders));

        if (trainings.isEmpty()) {
          return new ResponseEntity<>(new Message("List of trainings is empty ):"), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(trainings, HttpStatus.OK);
      } catch (Exception e) {
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }
    

    /*
     * GETTING ALL TRAININGS
     * URL : http://127.0.0.1:8080/trainings
     * PAGINATION AND SORTING :
     * URL : http://127.0.0.1:8080/trainings?page=index
     * URL : http://127.0.0.1:8080/trainings?size=number
     * URL : http://127.0.0.1:8080/trainings?page=index&size=number
     * URL : http://127.0.0.1:8080/trainings?page=index&size=number&sort=registered,desc&sort=name,asc
     * URL : http://127.0.0.1:8080/trainings?sort=name,asc
     * URL : http://127.0.0.1:8080/trainings?sort=registered,desc&sort=name,asc
     */
    @GetMapping("/trainings")
    public ResponseEntity<Map<String, Object>> getAllTrainingsPage(
        @RequestParam(required = false) String name,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "3") int size,
        @RequestParam(defaultValue = "id,desc") String[] sort) {

      try {
        List<Order> orders = new ArrayList<Order>();

        if (sort[0].contains(",")) {
          for (String sortOrder : sort) {
            String[] _sort = sortOrder.split(",");
            orders.add(new Order(getSortDirection(_sort[1]), _sort[0]));
          }
        } else {
          // sort=[field, direction]
          orders.add(new Order(getSortDirection(sort[1]), sort[0]));
        }

        List<Training> trainings = new ArrayList<>();
        Pageable pagingSort = PageRequest.of(page, size, Sort.by(orders));

        Page<Training> pageTrainings;
        if (name == null)
        	pageTrainings = trainingRepository.findAll(pagingSort);
        else
        	pageTrainings = trainingRepository.findByNameContains(name, pagingSort);

        trainings = pageTrainings.getContent();

        Map<String, Object> response = new HashMap<>();
        response.put("trainings", trainings);
        response.put("currentPage", pageTrainings.getNumber());
        response.put("totalItems", pageTrainings.getTotalElements());
        response.put("totalPages", pageTrainings.getTotalPages());

        return new ResponseEntity<>(response, HttpStatus.OK);
      } catch (Exception e) {
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }
    
    /*
     * GET THE LIST OF TRAINING COURSES START
     * URL : http://127.0.0.1:8080/trainings/started
     */
    @GetMapping("/trainings/started")
    public ResponseEntity<Map<String, Object>> findByStarted(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "3") int size) {
      
      try {
        List<Training> trainings = new ArrayList<>();
        Pageable paging = PageRequest.of(page, size);

        Page<Training> pageTrainings = trainingRepository.findByStarted(true, paging);
        trainings = pageTrainings.getContent();

        Map<String, Object> response = new HashMap<>();
        response.put("trainings", trainings);
        response.put("currentPage", pageTrainings.getNumber());
        response.put("totalItems", pageTrainings.getTotalElements());
        response.put("totalPages", pageTrainings.getTotalPages());

        return new ResponseEntity<>(response, HttpStatus.OK);
      } catch (Exception e) {
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

    /*
     * GETTING A SINGLE TRAINING BY ID
     * URL : http://127.0.0.1:8080/trainings/detail/{trainingId}
     */
    @GetMapping(path = "/trainings/detail/{trainingId}")
    public ResponseEntity<?> findTrainingById(@PathVariable("trainingId") Long trainingId){
        try {
        	if (!trainingRepository.existsById(trainingId))
                return new ResponseEntity<>(new Message("Training does not exist : "+trainingId+"!"), HttpStatus.NOT_FOUND);
            Training training = trainingRepository.findById(trainingId).get();
            return new ResponseEntity<>(training, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(new Message("ERROR REQUEST!"), HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }

   
    /*
     * GETTING A SINGLE TRAINING BY NAME
     * URL : http://127.0.0.1:8080/trainings/detail-name/{trainingName}
     */
    @GetMapping(path = "/trainings/detail-name/{trainingName}")
    public ResponseEntity<?> getTrainingByName(@PathVariable String trainingName){
       try {
    	   if (!trainingRepository.existsByName(trainingName))
               return new ResponseEntity<>(new Message("Training does not exist : "+trainingName), HttpStatus.NOT_FOUND);
           Training training = trainingRepository.findByNameContaining(trainingName).get();
           return new ResponseEntity<>(training, HttpStatus.OK);
	   } catch (Exception e) {
		   return new ResponseEntity<>(new Message("ERROR REQUEST!"), HttpStatus.INTERNAL_SERVER_ERROR);
	   }
    }
    
    
    /*
     * SEARCH ALL PRODUCTS FROM DATABASE
     * URL : http://127.0.0.1:8080/trainings/search/{keyword}
     */
    @GetMapping(path = "/trainings/search/{keyword}")
    public ResponseEntity<?> searchProductsByKeyword(@PathVariable("keyword") String keyword){
    	try {
    		List<Training> trainings = trainingRepository.findByKeyword(keyword);
       	 if (trainings.isEmpty())
                return new ResponseEntity<>(new Message("Sorry, but we haven't heard back!"), HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(trainings, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }
    
    /*
     * DELETE A TRAINING BY ID FROM DATABASE
     * URL : http://127.0.0.1:8080/trainings/delete/{trainingId}
     */
    @DeleteMapping(path = "/trainings/delete/{trainingId}")
    public ResponseEntity<?> deleteTrainingById(@PathVariable("trainingId") Long trainingId){
       try {
    	   if (!trainingRepository.existsById(trainingId))
               return new ResponseEntity<>(new Message("Training does not exist : "+trainingId), HttpStatus.NOT_FOUND);
           trainingRepository.deleteById(trainingId);
           return new ResponseEntity<>(new Message("Training has been deleted successfully : "+trainingId), HttpStatus.OK);
	   } catch (Exception e) {
		   return new ResponseEntity<>(new Message("ERROR REQUEST!"), HttpStatus.INTERNAL_SERVER_ERROR);
	   }
    }

    /*
     * POST A NEW TRAINING FROM DATABASE
     * URL : http://127.0.0.1:8080/trainings/add
     */
    @PostMapping(path = "/trainings/add")
    public ResponseEntity<?> saveTraining(@Valid @RequestBody TrainingDto dto){
       try {
    	   if (trainingRepository.existsByName(dto.getName()))
               return new ResponseEntity<>(new Message("Name of training is already exist!"), HttpStatus.BAD_REQUEST);
          if (dto.getName().isBlank()){
               return new ResponseEntity<>(new Message("This field is required!"), HttpStatus.BAD_REQUEST);
          }

          Training training = new Training(dto.getName(), dto.getDuration(), dto.isStarted());
          trainingRepository.save(training);
          return new ResponseEntity<>(new Message("Training has been added successfully : "+training.getName()), HttpStatus.CREATED);
	   } catch (Exception e) {
		   return new ResponseEntity<>(new Message("ERROR REQUEST!"), HttpStatus.INTERNAL_SERVER_ERROR);
	   }
    }


    /*
     * UPDATE A TRAINING BY ID FROM DATABASE
     * URL : http://127.0.0.1:8080/trainings/update/{trainingId}
     */
    @PutMapping(path = "/trainings/update/{trainingId}")
    public ResponseEntity<?> updateTrainingById(@PathVariable Long trainingId, @Valid @RequestBody TrainingDto dto){
        try {
        	if (!trainingRepository.existsById(trainingId))
                return new ResponseEntity<>(new Message("Training does not exist : "+trainingId), HttpStatus.NOT_FOUND);
            if (trainingRepository.existsByName(dto.getName()) && trainingRepository.findByNameContaining(dto.getName()).get().getId() != trainingId)
                return new ResponseEntity<>(new Message("Name of training is already exist!"), HttpStatus.BAD_REQUEST);
            if (dto.getName().isBlank()){
                return new ResponseEntity<>(new Message("This field is required!"), HttpStatus.BAD_REQUEST);
            }

            Training training = trainingRepository.findById(trainingId).get();
            training.setName(dto.getName());
            training.setDuration(dto.getDuration());
            training.setStarted(dto.isStarted());
            trainingRepository.save(training);
            return new ResponseEntity<>(new Message("Training has been updated successfully : "+trainingId), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(new Message("ERROR REQUEST!"), HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }
}
