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

    /* public List<ActionDTO> getAll() {
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
    } */

    public List<ActionDTO> getAll() {
    // 1. Get authenticated username
    UserDetails userDetails = 
        (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    String username = userDetails.getUsername();

    // 2. Load full user entity
    UtilisateurEntity user = userRepository.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé"));

    // 3. Get actions for this user only
    List<ActionEntity> userActions = repository.findByUser(user);

    // 4. Convert to DTOs
    List<ActionDTO> dtos = new ArrayList<>();
    for (ActionEntity action : userActions) {
        dtos.add(convertToDTO(action));
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

    



