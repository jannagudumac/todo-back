package com.descodeuses.planit.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.descodeuses.planit.dto.ActionDTO;
import com.descodeuses.planit.entity.ActionEntity;
import com.descodeuses.planit.entity.ContactEntity;
import com.descodeuses.planit.entity.ProjetEntity;
import com.descodeuses.planit.entity.UtilisateurEntity;
import com.descodeuses.planit.repository.ActionRepository;
import com.descodeuses.planit.repository.ContactRepository;
import com.descodeuses.planit.repository.ProjetRepository;
import com.descodeuses.planit.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ActionService {

    private final ActionRepository repository;
    private final ContactRepository contactRepository;
    private final ProjetRepository projetRepository;
    @Autowired
    private UserRepository userRepository;

    public ActionService(ActionRepository repository, ContactRepository contactRepository,
            ProjetRepository projetRepository) {
        this.repository = repository;
        this.contactRepository = contactRepository;
        this.projetRepository = projetRepository;
    }

    private ActionDTO convertToDTO(ActionEntity action) {
        ActionDTO dto = new ActionDTO(
                action.getId(),
                action.getTitle(),
                action.getCompleted(),
                action.getDueDate(),
                action.getDescription(),
                action.getPriority());

        Set<Long> memberIds = action.getMembers().stream()
                .map(ContactEntity::getId)
                .collect(Collectors.toSet());

        dto.setMemberIds(memberIds);
        dto.setProjetId(action.getProjet() != null ? action.getProjet().getId() : null);

        return dto;
    }

    private ActionEntity convertToEntity(ActionDTO actionDTO, Set<ContactEntity> members) {
        ActionEntity action = new ActionEntity();
        action.setId(actionDTO.getId());
        action.setTitle(actionDTO.getTitle());
        action.setCompleted(actionDTO.getCompleted());
        action.setDueDate(actionDTO.getDueDate());
        action.setMembers(members);
        action.setPriority(actionDTO.getPriority());
        action.setDescription(actionDTO.getDescription());

        if (actionDTO.getProjetId() != null) {
            ProjetEntity projet = projetRepository.findById(actionDTO.getProjetId())
                    .orElseThrow(
                            () -> new EntityNotFoundException("Projet not found with id: " + actionDTO.getProjetId()));
            action.setProjet(projet);
        } else {
            action.setProjet(null);
        }

        return action;
    }

    public List<ActionDTO> getAll() {
        List<ActionEntity> entities = repository.findAll();
        // Declarer une variable liste de action DTO
        List<ActionDTO> dtos = new ArrayList<>();

        // Faire boucle sur la liste action
        for (ActionEntity item : entities) {
            // Convertir action vers action dto
            // Ajouter action dto dans la liste
            dtos.add(convertToDTO(item));
        }
        return dtos;
    }

    public ActionDTO getActionById(Long id) {
        // Version courte
        /*
         * Action action =
         * repository
         * .findById(id)
         * .orElseThrow(() -> new EntityNotFoundException("Action not found with id: " +
         * id));
         * 
         * return convertToDTO(action);
         */

        // Version longue explicite
        Optional<ActionEntity> action = repository.findById(id);

        if (action.isEmpty()) {
            throw new EntityNotFoundException("Action not found with id: " + id);
        }
        ActionEntity entity = action.get();
        /*
         * ActionDTO dto = convertToDTO(entity);
         * Set<Long> ids = entity.getMembers()
         * .stream()
         * .map(ContactEntity::getId)
         * .collect(Collectors.toSet());
         * entity.setMembers(entity.getMembers());
         */

        return convertToDTO(entity);
    }

  public ActionDTO create(ActionDTO dto) {
    // 1. Récupérer les contacts associés
    Set<ContactEntity> contacts = new HashSet<>();
    if (dto.getMemberIds() != null) {
        contacts.addAll(contactRepository.findAllById(dto.getMemberIds()));
    }

    // 2. Récupérer l'utilisateur connecté via Spring Security
    UserDetails userDetails =
        (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    String username = userDetails.getUsername();

    // 3. Trouver l'utilisateur en base via son nom d'utilisateur
    UtilisateurEntity user = userRepository.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé"));

    // 4. Convertir le DTO en entité métier, avec les contacts
    ActionEntity entity = convertToEntity(dto, contacts);

    // 5. Associer l'utilisateur connecté à l'entité Action
    entity.setUser(user);

    // 6. Sauvegarder l'entité dans la base de données
    ActionEntity savedEntity = repository.save(entity);

    // 7. Convertir l'entité enregistrée en DTO et retourner
    return convertToDTO(savedEntity);
}







    public ActionDTO update(Long id, ActionDTO dto) {
        // Rechercher l'entité par son identifiant
        // entitéExistante = repo.trouverParId(id)
        // sinon lever une exception "Ressource non trouvée"
        ActionEntity existingEntity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Action not found with id: " + id));

        // Mettre à jour les champs de l'entité avec les valeurs du DTO
        existingEntity.setTitle(dto.getTitle());
        existingEntity.setCompleted(dto.getCompleted());
        existingEntity.setDueDate(dto.getDueDate());
        existingEntity.setPriority(dto.getPriority());
        existingEntity.setDescription(dto.getDescription());

        Set<ContactEntity> contacts = new HashSet<>(contactRepository.findAllById(dto.getMemberIds())); // récupérer les
                                                                                                        // bons membres
        existingEntity.setMembers(contacts);

        // update projet
        if (dto.getProjetId() != null) {
            ProjetEntity projet = projetRepository.findById(dto.getProjetId())
                    .orElseThrow(() -> new EntityNotFoundException("Projet not found with id: " + dto.getProjetId()));
            existingEntity.setProjet(projet);
        } else {
            existingEntity.setProjet(null);
        }

        // Sauvegarder l'entité mise à jour dans la base de données
        ActionEntity updatedEntity = repository.save(existingEntity);

        // Convertir l'entité mise à jour en DTO et retourner
        return convertToDTO(updatedEntity);
    }

    public void delete(Long id) {
        // Vérifier si une entité avec l'identifiant donné existe
        // si référentiel.n'existePasParId(id) alors
        // lever une exception "Ressource non trouvée avec cet id"
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Action not found with id: " + id);
        }

        // Supprimer l'entité par son identifiant
        // référentiel.supprimerParId(id)
        repository.deleteById(id);
    }


}

    
    /* public ActionDTO create(ActionDTO dto) {
        // Convertir le DTO en entité
        // entité = convertirVersEntité(dto)
        Set<ContactEntity> contacts = new HashSet<>();
        if (dto.getMemberIds() != null) {
            contacts.addAll(contactRepository.findAllById(dto.getMemberIds()));
        }

        // récupérer les bons membres
        ActionEntity entity = convertToEntity(dto, contacts);

        // Sauvegarder l'entité dans la base de données
        // entitéEnregistrée = référentiel.sauvegarder(entité)
        ActionEntity savedEntity = repository.save(entity);

        // Convertir l'entité enregistrée en DTO et retourner
        // retourner convertirVersDTO(entitéEnregistrée)
        return convertToDTO(savedEntity);
        
        EXAMPLE
        UserDetails userDetails = 
            (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        
        DCUser user = userRepository.findByUsername(username)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Todo todo = convertToEntity(todoDTO);
        todo.setUtilisateur(user);

        Todo savedTodo = todoRepository.save(todo);
        return convertToDTO(savedTodo);

        EXAMPLE
       

        
    } */





        /*
         package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.DTO.TodoDTO;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.DCUser;
import com.example.demo.model.Todo;
import com.example.demo.repository.TodoRepository;
import com.example.demo.repository.UserRepository;

@Service
public class TodoService {

    private final TodoRepository todoRepository;

    @Autowired
    private UserRepository userRepository;

    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public List<TodoDTO> getAllTodos() {
        List<Todo> todos = todoRepository.findAll(Sort.by(Sort.Direction.DESC, "dueDate"));
        List<TodoDTO> todoDTOs = new ArrayList<>();
        for (Todo todo : todos) {
            todoDTOs.add(convertToDTO(todo));
        }
        return todoDTOs;
        //return todoRepository.findAll(Sort.by(Sort.Direction.DESC, "dueDate")).stream()
        //        .map(this::convertToDTO)
        //        .collect(Collectors.toList());
    }

    public TodoDTO getTodoById(Long id) {
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Todo not found with id: " + id));
        return convertToDTO(todo);
    }

    public TodoDTO createTodo(TodoDTO todoDTO) {
        UserDetails userDetails = 
            (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        
        DCUser user = userRepository.findByUsername(username)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Todo todo = convertToEntity(todoDTO);
        todo.setUtilisateur(user);

        Todo savedTodo = todoRepository.save(todo);
        return convertToDTO(savedTodo);
    }

    public TodoDTO updateTodo(Long id, TodoDTO todoDTO) {
        Todo existingTodo = todoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Todo not found with id: " + id));
        
        existingTodo.setTitle(todoDTO.getTitle());
        existingTodo.setCompleted(todoDTO.isCompleted());
        existingTodo.setPriorite(todoDTO.getPriorite());
        existingTodo.setDueDate(todoDTO.getDueDate());
        
        Todo updatedTodo = todoRepository.save(existingTodo);
        return convertToDTO(updatedTodo);
    }

    public void deleteTodo(Long id) {
        if (!todoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Todo not found with id: " + id);
        }
        todoRepository.deleteById(id);
    }

    // Helper method to convert Entity to DTO
    private TodoDTO convertToDTO(Todo todo) {
        return new TodoDTO(
                todo.getId(),
                todo.getTitle(),
                todo.isCompleted(),
                todo.getPriorite(),
                todo.getDueDate()
        );
    }

    // Helper method to convert DTO to Entity
    private Todo convertToEntity(TodoDTO todoDTO) {
        Todo todo = new Todo();
        // Don't set ID for new entities
        if (todoDTO.getId() != null) {
            todo.setId(todoDTO.getId());
        }
        todo.setTitle(todoDTO.getTitle());
        todo.setCompleted(todoDTO.isCompleted());
        todo.setPriorite(todoDTO.getPriorite());
        todo.setDueDate(todoDTO.getDueDate());
        return todo;
    }
}
         */

