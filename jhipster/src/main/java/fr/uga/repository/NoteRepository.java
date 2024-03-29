package fr.uga.repository;

import fr.uga.domain.Note;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Note entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {
    @Query("select note from Note note where note.user.login = ?#{principal.username}")
    List<Note> findByUserIsCurrentUser();
    void deleteByPatient_Id(Long id);

    List<Note> findByPatient_IdAndUser_LoginOrderByDateDesc(Long patientId, String login);
}
