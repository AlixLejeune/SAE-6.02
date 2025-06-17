package com.SAE.sae.service.RoomObjects;

import com.SAE.sae.entity.RoomObjects.DataTable;
import com.SAE.sae.repository.RoomObjects.DataTableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class DataTableManager {

    private final DataTableRepository dataTableRepository;

    @Autowired
    public DataTableManager(DataTableRepository dataTableRepository) {
        this.dataTableRepository = dataTableRepository;
    }

    // ========= CREATE / UPDATE =========

    /**
     * Créer ou mettre à jour une DataTable
     */
    public DataTable save(DataTable dataTable) {
        return dataTableRepository.save(dataTable);
    }

    /**
     * Créer ou mettre à jour une liste de DataTables
     */
    public List<DataTable> saveAll(List<DataTable> dataTables) {
        return dataTableRepository.saveAll(dataTables);
    }

    // ========= READ =========

    /**
     * Récupérer une DataTable par ID
     */
    public Optional<DataTable> findById(Integer id) {
        return dataTableRepository.findById(id);
    }

    /**
     * Récupérer toutes les DataTables
     */
    public List<DataTable> findAll() {
        return dataTableRepository.findAll();
    }

    /**
     * Récupérer les DataTables par ID de salle
     */
    public List<DataTable> findByRoomId(Integer  roomId) {
        return dataTableRepository.findByRoomId(roomId);
    }

    /**
     * Récupérer les DataTables via l'objet Room
     */
    public List<DataTable> findByRoomId(Long roomId) {
        return dataTableRepository.findByRoom_Id(roomId);
    }

    /**
     * Récupérer les DataTables par nom personnalisé
     */
    public List<DataTable> findByCustomName(String customName) {
        return dataTableRepository.findByCustomName(customName);
    }

    // ========= DELETE =========

    /**
     * Supprimer une DataTable par ID
     */
    public void deleteById(Integer id) {
        dataTableRepository.deleteById(id);
    }

    /**
     * Supprimer un objet DataTable
     */
    public void delete(DataTable dataTable) {
        dataTableRepository.delete(dataTable);
    }

    /**
     * Supprimer toutes les DataTables
     */
    public void deleteAll() {
        dataTableRepository.deleteAll();
    }

    /**
     * Supprimer toutes les DataTables d'une salle
     */
    @Transactional
    public void deleteByRoomId(Integer roomId) {
        dataTableRepository.deleteByRoomId(roomId);
    }

    /**
     * Supprimer les DataTables par nom personnalisé
     */
    @Transactional
    public void deleteByCustomName(String customName) {
        dataTableRepository.deleteByCustomName(customName);
    }

    // ========= AUTRES =========

    /**
     * Vérifier l'existence d'une DataTable par ID
     */
    public boolean existsById(Integer id) {
        return dataTableRepository.existsById(id);
    }

    /**
     * Compter le nombre total de DataTables
     */
    public long count() {
        return dataTableRepository.count();
    }
}
