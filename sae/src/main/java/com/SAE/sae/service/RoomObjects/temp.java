package com.SAE.sae.service;

import com.SAE.sae.entity.Room;
import com.SAE.sae.entity.RoomObjects.Door;
import com.SAE.sae.repository.DoorRepository;
import com.SAE.sae.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class DoorService {

    @Autowired
    private DoorRepository doorRepository;
    
    @Autowired
    private RoomObjectRepository roomRepository;

    /**
     * CREATE - Créer une nouvelle porte
     */
    public Door createDoor(Door door) {
        validateDoor(door);
        return doorRepository.save(door);
    }
    
    /**
     * CREATE - Créer une porte avec ID de salle
     */
    public Door createDoor(Door door, Number roomId) {
        validateDoor(door);
        
        // Associer la salle si l'ID est fourni
        if (roomId != null) {
            Optional<Room> room = roomRepository.findById(roomId.longValue());
            if (room.isPresent()) {
                door.setRoom(room.get());
                door.IdRoom = roomId;
            } else {
                throw new RuntimeException("Salle avec l'ID " + roomId + " introuvable");
            }
        }
        
        return doorRepository.save(door);
    }

    /**
     * READ - Récupérer toutes les portes
     */
    @Transactional(readOnly = true)
    public List<Door> getAllDoors() {
        return doorRepository.findAll();
    }

    /**
     * READ - Récupérer une porte par son ID
     */
    @Transactional(readOnly = true)
    public Optional<Door> getDoorById(Number id) {
        return doorRepository.findById(id.longValue());
    }
    
    /**
     * READ - Récupérer une porte par son ID avec exception si non trouvée
     */
    @Transactional(readOnly = true)
    public Door getDoorByIdOrThrow(Number id) {
        return doorRepository.findById(id.longValue())
                .orElseThrow(() -> new RuntimeException("Porte avec l'ID " + id + " introuvable"));
    }

    /**
     * READ - Récupérer toutes les portes d'une salle
     */
    @Transactional(readOnly = true)
    public List<Door> getDoorsByRoomId(Number roomId) {
        return doorRepository.findByIdRoom(roomId);
    }
    
    /**
     * READ - Récupérer les portes par nom personnalisé
     */
    @Transactional(readOnly = true)
    public List<Door> getDoorsByCustomName(String customName) {
        return doorRepository.findByCustomNameContainingIgnoreCase(customName);
    }
    
    /**
     * READ - Récupérer les portes dans une zone spécifique
     */
    @Transactional(readOnly = true)
    public List<Door> getDoorsInArea(Double minX, Double maxX, Double minY, Double maxY) {
        return doorRepository.findDoorsInArea(minX, maxX, minY, maxY);
    }

    /**
     * UPDATE - Mettre à jour une porte existante
     */
    public Door updateDoor(Number id, Door updatedDoor) {
        Door existingDoor = getDoorByIdOrThrow(id);
        
        // Mise à jour des propriétés
        if (updatedDoor.getCustomName() != null) {
            existingDoor.setCustomName(updatedDoor.getCustomName());
        }
        if (updatedDoor.getPosX() != null) {
            existingDoor.setPosX(updatedDoor.getPosX());
        }
        if (updatedDoor.getPosY() != null) {
            existingDoor.setPosY(updatedDoor.getPosY());
        }
        if (updatedDoor.getPosZ() != null) {
            existingDoor.setPosZ(updatedDoor.getPosZ());
        }
        if (updatedDoor.getSizeX() != null) {
            existingDoor.setSizeX(updatedDoor.getSizeX());
        }
        if (updatedDoor.getSizeY() != null) {
            existingDoor.setSizeY(updatedDoor.getSizeY());
        }
        if (updatedDoor.getSizeZ() != null) {
            existingDoor.setSizeZ(updatedDoor.getSizeZ());
        }
        if (updatedDoor.getRoom() != null) {
            existingDoor.setRoom(updatedDoor.getRoom());
            existingDoor.IdRoom = updatedDoor.getRoom().getId();
        }
        
        validateDoor(existingDoor);
        return doorRepository.save(existingDoor);
    }
    
    /**
     * UPDATE - Mettre à jour la position d'une porte
     */
    public Door updateDoorPosition(Number id, Double posX, Double posY, Double posZ) {
        Door door = getDoorByIdOrThrow(id);
        door.setPosX(posX);
        door.setPosY(posY);
        door.setPosZ(posZ);
        validateDoorPosition(door);
        return doorRepository.save(door);
    }
    
    /**
     * UPDATE - Mettre à jour la taille d'une porte
     */
    public Door updateDoorSize(Number id, Double sizeX, Double sizeY, Double sizeZ) {
        Door door = getDoorByIdOrThrow(id);
        door.setSizeX(sizeX);
        door.setSizeY(sizeY);
        door.setSizeZ(sizeZ);
        validateDoorSize(door);
        return doorRepository.save(door);
    }
    
    /**
     * UPDATE - Déplacer une porte vers une autre salle
     */
    public Door moveDoorToRoom(Number doorId, Number roomId) {
        Door door = getDoorByIdOrThrow(doorId);
        Room room = roomRepository.findById(roomId.longValue())
                .orElseThrow(() -> new RuntimeException("Salle avec l'ID " + roomId + " introuvable"));
        
        door.setRoom(room);
        door.IdRoom = roomId;
        return doorRepository.save(door);
    }

    /**
     * DELETE - Supprimer une porte par son ID
     */
    public void deleteDoor(Number id) {
        if (!doorRepository.existsById(id.longValue())) {
            throw new RuntimeException("Porte avec l'ID " + id + " introuvable");
        }
        doorRepository.deleteById(id.longValue());
    }
    
    /**
     * DELETE - Supprimer toutes les portes d'une salle
     */
    public void deleteAllDoorsInRoom(Number roomId) {
        List<Door> doors = getDoorsByRoomId(roomId);
        doorRepository.deleteAll(doors);
    }
    
    /**
     * DELETE - Supprimer toutes les portes
     */
    public void deleteAllDoors() {
        doorRepository.deleteAll();
    }

    /**
     * UTILITY - Vérifier si une porte existe
     */
    @Transactional(readOnly = true)
    public boolean existsById(Number id) {
        return doorRepository.existsById(id.longValue());
    }
    
    /**
     * UTILITY - Compter le nombre total de portes
     */
    @Transactional(readOnly = true)
    public long countAllDoors() {
        return doorRepository.count();
    }
    
    /**
     * UTILITY - Compter les portes dans une salle
     */
    @Transactional(readOnly = true)
    public long countDoorsInRoom(Number roomId) {
        return doorRepository.countByIdRoom(roomId);
    }

    /**
     * VALIDATION - Valider une porte complètement
     */
    private void validateDoor(Door door) {
        if (door == null) {
            throw new IllegalArgumentException("La porte ne peut pas être null");
        }
        
        validateDoorPosition(door);
        validateDoorSize(door);
        validateCustomName(door.getCustomName());
    }
    
    /**
     * VALIDATION - Valider la position d'une porte
     */
    private void validateDoorPosition(Door door) {
        if (door.getPosX() == null || door.getPosY() == null || door.getPosZ() == null) {
            throw new IllegalArgumentException("Les coordonnées de position ne peuvent pas être null");
        }
        
        // Validation des limites de position (exemple)
        if (door.getPosX() < 0 || door.getPosY() < 0 || door.getPosZ() < 0) {
            throw new IllegalArgumentException("Les coordonnées de position doivent être positives");
        }
    }
    
    /**
     * VALIDATION - Valider la taille d'une porte
     */
    private void validateDoorSize(Door door) {
        if (door.getSizeX() == null || door.getSizeY() == null || door.getSizeZ() == null) {
            throw new IllegalArgumentException("Les dimensions ne peuvent pas être null");
        }
        
        if (door.getSizeX() <= 0 || door.getSizeY() <= 0 || door.getSizeZ() <= 0) {
            throw new IllegalArgumentException("Les dimensions doivent être positives");
        }
        
        // Validation des limites de taille pour une porte (exemple)
        if (door.getSizeX() > 10 || door.getSizeY() > 5 || door.getSizeZ() > 1) {
            throw new IllegalArgumentException("Les dimensions de la porte dépassent les limites maximales");
        }
    }
    
    /**
     * VALIDATION - Valider le nom personnalisé
     */
    private void validateCustomName(String customName) {
        if (customName != null && customName.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom personnalisé ne peut pas être vide");
        }
        
        if (customName != null && customName.length() > 100) {
            throw new IllegalArgumentException("Le nom personnalisé ne peut pas dépasser 100 caractères");
        }
    }
}