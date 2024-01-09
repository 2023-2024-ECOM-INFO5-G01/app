package fr.uga.web.rest;

import fr.uga.domain.Patient;
import fr.uga.repository.PatientRepository;
import fr.uga.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import fr.uga.repository.IMCRepository;
import fr.uga.repository.PoidsRepository;
import fr.uga.repository.AlbumineRepository;
import fr.uga.domain.Note;
import fr.uga.repository.NoteRepository;
import fr.uga.domain.IMC;
import fr.uga.domain.Poids;
import fr.uga.domain.Albumine;
import java.time.LocalDate;
import java.time.ZoneId;
import fr.uga.domain.Alerte;
import java.time.Instant;
import fr.uga.repository.AlerteRepository;
import fr.uga.domain.User;
import fr.uga.repository.EPARepository;
import fr.uga.domain.EPA;
import fr.uga.repository.RappelRepository;
import fr.uga.domain.Rappel;
import java.time.Duration;
import org.springframework.scheduling.annotation.Scheduled;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;


/**
 * REST controller for managing {@link fr.uga.domain.Patient}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class PatientResource {

	private final Logger log = LoggerFactory.getLogger(PatientResource.class);

	private static final String ENTITY_NAME = "patient";

	@Value("${jhipster.clientApp.name}")
	private String applicationName;

	private final PatientRepository patientRepository;

	private final IMCRepository imcRepository;

	private final AlbumineRepository albumineRepository;

	private final PoidsRepository poidsRepository;

	private final AlerteRepository alerteRepository;

	private final EPARepository epaRepository;

	private final RappelRepository rappelRepository;

	private final NoteRepository noteRepository;

	public PatientResource(PatientRepository patientRepository, IMCRepository imcRepository, AlbumineRepository albumineRepository, PoidsRepository poidsRepository, AlerteRepository alerteRepository, EPARepository epaRepository, RappelRepository rappelRepository, NoteRepository noteRepository) {
		this.patientRepository = patientRepository;
		this.imcRepository = imcRepository;
		this.albumineRepository = albumineRepository;
		this.poidsRepository = poidsRepository;
		this.alerteRepository = alerteRepository;
		this.epaRepository = epaRepository;
		this.rappelRepository = rappelRepository;
		this.noteRepository = noteRepository;
	}

	/**
	 * {@code POST  /patients} : Create a new patient.
	 *
	 * @param patient the patient to create.
	 * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
	 *         body the new patient, or with status {@code 400 (Bad Request)} if the
	 *         patient has already an ID.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
	@PostMapping("/patients")
	public ResponseEntity<Patient> createPatient(@RequestBody Patient patient) throws URISyntaxException {
		log.debug("REST request to save Patient : {}", patient);
		if (patient.getId() != null) {
			throw new BadRequestAlertException("A new patient cannot already have an ID", ENTITY_NAME, "idexists");
		}
		Patient result = patientRepository.save(patient);
		log.info("Patient created: {}", result);
		return ResponseEntity
				.created(new URI("/api/patients/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME,
						result.getId().toString()))
				.body(result);
	}

	/**
	 * {@code PUT  /patients/:id} : Updates an existing patient.
	 *
	 * @param id      the id of the patient to save.
	 * @param patient the patient to update.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the updated patient,
	 *         or with status {@code 400 (Bad Request)} if the patient is not valid,
	 *         or with status {@code 500 (Internal Server Error)} if the patient
	 *         couldn't be updated.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
	@PutMapping("/patients/{id}")
	public ResponseEntity<Patient> updatePatient(@PathVariable(value = "id", required = false) final Long id,
			@RequestBody Patient patient)
			throws URISyntaxException {
		log.debug("REST request to update Patient : {}, {}", id, patient);
		if (patient.getId() == null) {
			throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
		}
		if (!Objects.equals(id, patient.getId())) {
			throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
		}

		if (!patientRepository.existsById(id)) {
			throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
		}

		Patient result = patientRepository.save(patient);
		return ResponseEntity
				.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME,
						patient.getId().toString()))
				.body(result);
	}

	/**
	 * {@code PATCH  /patients/:id} : Partial updates given fields of an existing
	 * patient, field will ignore if it is null
	 *
	 * @param id      the id of the patient to save.
	 * @param patient the patient to update.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the updated patient,
	 *         or with status {@code 400 (Bad Request)} if the patient is not valid,
	 *         or with status {@code 404 (Not Found)} if the patient is not found,
	 *         or with status {@code 500 (Internal Server Error)} if the patient
	 *         couldn't be updated.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
	@PatchMapping(value = "/patients/{id}", consumes = { "application/json", "application/merge-patch+json" })
	public ResponseEntity<Patient> partialUpdatePatient(
		@PathVariable(value = "id", required = false) final Long id,
		@RequestBody Patient patient
	) throws URISyntaxException { 
		log.debug("REST request to partial update Patient partially : {}, {}", id, patient);
		if (patient.getId() == null) {
			throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
		}
		if (!Objects.equals(id, patient.getId())) {
			throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
		}

		if (!patientRepository.existsById(id)) {
			throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
		}

		Optional<Patient> result = patientRepository
				.findById(patient.getId())
				.map(existingPatient -> {
					if (patient.getNom() != null) {
						existingPatient.setNom(patient.getNom());
					}
					if (patient.getPrenom() != null) {
						existingPatient.setPrenom(patient.getPrenom());
					}
					if (patient.getStatut() != null) {
						existingPatient.setStatut(patient.getStatut());
					}
					if (patient.getDateNaissance() != null) {
						existingPatient.setDateNaissance(patient.getDateNaissance());
					}
					if (patient.getTaille() != null) {
						existingPatient.setTaille(patient.getTaille());
					}
					if (patient.getDatearrive() != null) {
						existingPatient.setDatearrive(patient.getDatearrive());
					}

					return existingPatient;
				})
				.map(patientRepository::save);

		return ResponseUtil.wrapOrNotFound(
				result,
				HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, patient.getId().toString()));
	}

	/**
	 * {@code GET  /patients} : get all the patients.
	 *
	 * @param eagerload flag to eager load entities from relationships (This is
	 *                  applicable for many-to-many).
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
	 *         of patients in body.
	 */
	@GetMapping("/patients")
	public List<Patient> getAllPatients(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
		log.debug("REST request to get all Patients");
		if (eagerload) {
			return patientRepository.findAllWithEagerRelationships();
		} else {
			return patientRepository.findAll();
		}
	}

	/**
	 * {@code GET  /patients/suggestions} : get the patient that match the query.
	 * 
	 * @param query the query of the patient to retrieve.
	 * @param login the login of the user.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the patient, or with status {@code 404 (Not Found)}.
	 */
	@GetMapping("/patients/suggestions/{query}/{login}")
	public ResponseEntity<List<Patient>> suggestPatients(@PathVariable String query, @PathVariable String login) {
		log.debug("REST request to get Patients starting with: {}, with login: {}", query, login);
		List<Patient> patientsByNom = patientRepository.findByNomStartingWithIgnoreCaseAndUsers_Login(query, login);
		List<Patient> patientsByPrenom = patientRepository.findByPrenomStartingWithIgnoreCaseAndUsers_Login(query,
				login);
		Set<Patient> uniquePatients = new HashSet<>();
		uniquePatients.addAll(patientsByNom);
		uniquePatients.addAll(patientsByPrenom);

		// Convertissez l'ensemble en liste avant de renvoyer la réponse
		List<Patient> uniquePatientsList = new ArrayList<>(uniquePatients);
		log.info("Patients suggestions: {}", uniquePatientsList);
		return ResponseEntity.ok().body(uniquePatientsList);
	}

	/**
	 * GET /patients/user/{login} : get all the patients of a specific user.
	 *
	 * @param login the login of the user.
	 * @return the ResponseEntity with status 200 (OK) and the list of patients in
	 *         body.
	 */
	@GetMapping("/patients/user/{login}")
	public ResponseEntity<List<Patient>> getAllPatientsByUser(@PathVariable String login) {
		log.debug("REST request to get all Patients of user: {}", login);

		List<Patient> patients = patientRepository.findByUsers_Login(login);
		log.info("Patients of user: {}", patients);
		return ResponseEntity.ok().body(patients);
	}

	/**
	 * {@code GET  /patients/:id} : get the "id" patient.
	 *
	 * @param id the id of the patient to retrieve.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the patient, or with status {@code 404 (Not Found)}.
	 */
	@GetMapping("/patients/{id}")
	public ResponseEntity<Patient> getPatient(@PathVariable Long id) {
		log.debug("REST request to get Patient : {}", id);
		Optional<Patient> patient = patientRepository.findOneWithEagerRelationships(id);
		return ResponseUtil.wrapOrNotFound(patient);
	}

	/**
	 * {@code DELETE  /patients/:id} : delete the "id" patient.
	 *
	 * @param id the id of the patient to delete.
	 * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
	 */
	@DeleteMapping("/patients/{id}")
	public ResponseEntity<Void> deletePatient(@PathVariable Long id) {
		log.debug("REST request to delete Patient : {}", id);
		patientRepository.deleteById(id);
		return ResponseEntity
				.noContent()
				.headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
				.build();
	}

	/** 
	 * PUT /patient/updateStatut: update patient entity status.
	 * @param id the id of the patient to update status.
	 * @param status the status to change
	 * @return the ResponseEntity with status 200 (OK) and with body the updated patient,
		or with status 404 (Not Found) if the patient is not found.
	*/
	@PutMapping("/patients/updatestatut/{id}/{statut}")
	public ResponseEntity<Patient> updateStatus(@PathVariable Long id, @PathVariable String statut) {
		log.debug("REST request to update Patient : {} with statut : {}", id, statut);

		Optional<Patient> patientOptional = patientRepository.findById(id);
		if (!patientOptional.isPresent()) {
			return ResponseEntity.notFound().build();
		}

		Patient patient = patientOptional.get();
		patient.setStatut(statut);

		Patient result = patientRepository.save(patient);

		return ResponseEntity.ok()
			.headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, patient.getId().toString()))
			.body(result);
	}

/**
 * Crée une nouvelle alerte pour un patient spécifique.
 *
 * Cette méthode crée une nouvelle instance d'Alerte, définit ses propriétés et la sauvegarde dans la base de données.
 * L'action et le patient sont passés en paramètres. La date de l'alerte est définie sur l'heure actuelle, 
 * et la vérification de l'alerte est définie sur false. Les utilisateurs associés à l'alerte sont ceux associés au patient.
 *
 * @param action L'action à associer à l'alerte.
 * @param patient Le patient à associer à l'alerte.
 */
private void createAlerte(String action, Patient patient) {
	Alerte newAlerte = new Alerte();
	newAlerte.setAction(action);
	newAlerte.setDate(Instant.now());
	newAlerte.setVerif(false);
	Set<User> users = new HashSet<>(patient.getUsers());
	newAlerte.setUsers(users);
	newAlerte.setPatient(patient);

	alerteRepository.save(newAlerte);
}

/**
 * Évalue les rappels en retard pour des actions spécifiques.
 *
 * Cette méthode est programmée pour s'exécuter à 1h00 tous les jours.
 * Elle récupère tous les rappels de la base de données et vérifie chaque rappel non vérifié.
 * Si la date du rappel est antérieure à celle d'il y a deux jours et que l'action du rappel est l'une des suivantes : 
 * "prise de poids", "Regarder le dossier", "prise de epa", "prise de albumine", ou "Surveiller la prise daliments", 
 * une alerte est créée avec un message spécifique et liée au patient concerné.
 *
 * @return Une chaîne de caractères indiquant le résultat de l'évaluation. 
 *         Si un rappel correspond aux critères, retourne "Taches non faites pour ce patient [nom du patient] pour cette action : [action du rappel]".
 *         Si aucun rappel ne correspond aux critères, retourne "aucun rappel n'a été oublié".
 * @throws URISyntaxException Si une erreur de syntaxe URI se produit.
 */
@Scheduled(cron = "0 0 1 * * ?")
	public String evaluerRetard() throws URISyntaxException {
 log.debug("REST request to evaluate retard");
  log.info("REST request to evaluate retard");


		List <Rappel> toutrappel = rappelRepository.findAll();

	   Instant now = Instant.now(); // current date-time
Instant TwoDayAgo = now.minus(Duration.ofDays(2)); // date-time two day ago
for (Rappel rappel : toutrappel) {
	if (!rappel.getVerif()) {
		Instant rappelDate = rappel.getDate();
		if (rappelDate.isBefore(TwoDayAgo) && (rappel.getAction().equals("prise de poids") || rappel.getAction().equals("Regarder le dossier") || rappel.getAction().equals("prise de epa") || rappel.getAction().equals("prise de albumine") || rappel.getAction().equals("Surveiller la prise daliments") )){
			String action = "Taches non faites pour ce patient " + rappel.getPatient().getNom() + "  pour cette action :" + rappel.getAction();
			createAlerte(action, rappel.getPatient());
			log.debug("nouveau rappel");
			log.info("nouveau rappel");
			return action;
		}
	}
}
return "aucun rappel n'a été oublié";
}


/**
 * Évalue l'EPA (Échelle de Précarité des Aliments) d'un patient spécifique.
 *
 * @param id L'identifiant du patient à évaluer.
 * @return Une chaîne de caractères indiquant le résultat de l'évaluation. 
 *         Si le patient n'est pas trouvé, retourne "Patient not found".
 *         Si aucune donnée EPA n'est trouvée pour le patient, retourne "Pas de données EPA pour ce patient".
 *         Si l'EPA est inférieur à 7, une alerte est créée et la méthode retourne "EPA < 7 : epa : [valeur de l'EPA]".
 *         Si l'EPA est supérieur ou égal à 7, retourne "pas d'EPA".
 * @throws URISyntaxException Si une erreur de syntaxe URI se produit.
 */
@GetMapping("/patients/epa/malnutrition/{id}")
public String evaluerEPA(@PathVariable Long id) throws URISyntaxException {
	Optional<Patient> patientOptional = patientRepository.findById(id);
		if (!patientOptional.isPresent()) {
			return "Patient not found";
		}

		Patient patient = patientOptional.get();
		EPA latestepa = null;

		Optional <EPA> latestepa1 = epaRepository.findFirstByPatientIdOrderByDateDesc(id);
		if (!latestepa1.isPresent()) {
			return "Pas de données EPA pour ce patient";

		}
		else {
			 latestepa = latestepa1.get();
		}

	if (latestepa.getEpa() < 7) {
		String action = "EPA < 7 : " + "epa : " + latestepa.getEpa();
		createAlerte(action, patient);
		return action;
	}
	else {
		return "pas d'EPA";
	}
}

	/**
 * Évalue la malnutrition d'un patient spécifique.
 *
 * Cette méthode récupère le patient par son ID et vérifie si une alerte a déjà été créée pour ce patient aujourd'hui.
 * Si c'est le cas, elle retourne un message indiquant qu'une alerte a déjà été créée.
 * Sinon, elle récupère les dernières valeurs de IMC, Albumine et Poids du patient et évalue la malnutrition en fonction de ces valeurs.
 * Si une condition de malnutrition est remplie, une alerte est créée et le message d'alerte est retourné.
 * Si aucune condition de malnutrition n'est remplie, elle retourne "pas de dénutrition".
 *
 * @param id L'identifiant du patient à évaluer.
 * @return Une chaîne de caractères indiquant le résultat de l'évaluation. 
 *         Si le patient n'est pas trouvé, retourne "Patient not found".
 *         Si une alerte a déjà été créée pour ce patient aujourd'hui, retourne "Une alerte a déjà été créée pour ce patient aujourd'hui".
 *         Si une condition de malnutrition est remplie, retourne le message d'alerte.
 *         Si aucune condition de malnutrition n'est remplie, retourne "pas de dénutrition".
 * @throws URISyntaxException Si une erreur de syntaxe URI se produit.
 */
	@GetMapping("/patients/malnutrition/{id}")
public String evaluerMalnutrition(@PathVariable Long id) throws URISyntaxException {
	Optional<Patient> patientOptional = patientRepository.findById(id);
		if (!patientOptional.isPresent()) {
			return "Patient not found";
		}

		Patient patient = patientOptional.get();
		LocalDateTime startOfDay = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
		ZonedDateTime zdt = startOfDay.atZone(ZoneId.systemDefault());
		Instant startOfDayInstant = zdt.toInstant();

		List<Alerte> alertes = alerteRepository.findByPatientIdAndDateAfter(id, startOfDayInstant);
		if (!alertes.isEmpty()) {
    		return "Une alerte a déjà été créée pour ce patient aujourd'hui";
		}

IMC latestimc = null;
	Albumine albumine = null;
	Poids poidsOneMonthago = null;
	Poids poidsSixMonthago = null;
	Poids latestPoids = null;

		Optional<IMC> latestimc1 = imcRepository.findFirstByPatientIdOrderByDateDesc(id);
		if (latestimc1.isPresent()) {
			 latestimc = latestimc1.get();
		}
		Optional <Albumine> albumine1 = albumineRepository.findFirstByPatientIdOrderByDateDesc(id);
		if (albumine1.isPresent()) {
			 albumine = albumine1.get();
		}
		ZoneId defaultZoneId = ZoneId.systemDefault();

List<Poids> poidsList = poidsRepository.findByPatientIdAndDateBeforeOrderByDateDesc(id, LocalDate.now().minusMonths(1).atStartOfDay(defaultZoneId).toInstant());
if (!poidsList.isEmpty()) {
	poidsOneMonthago = poidsList.get(0);
}
List <Poids> poidsSixMonthago1 = poidsRepository.findByPatientIdAndDateBeforeOrderByDateDesc(id, LocalDate.now().minusMonths(6).atStartOfDay(defaultZoneId).toInstant());
if (!poidsSixMonthago1.isEmpty()) {
	poidsSixMonthago = poidsSixMonthago1.get(0);
}


	   Optional <Poids> latestPoids1 = poidsRepository.findFirstByPatientIdOrderByDateDesc(id);
		if (latestPoids1.isPresent()) {
			 latestPoids = latestPoids1.get();
		}

 if (latestimc != null && latestimc.getImc() <= 17 ) {
	String action = "dénutrition sévère car IMC est inférieur ou égal à 17 : " + "imc : " + latestimc.getImc();
	createAlerte(action, patient);
	return action;
}
else if (latestPoids != null && poidsOneMonthago != null && latestPoids.getPoids() < poidsOneMonthago.getPoids() * 0.9 ) {
	String action = "dénutrition sévère car le poids a diminué de plus de 10% en un mois : " + "poids : " + latestPoids.getPoids() + " poids 1 mois avant : " + poidsOneMonthago.getPoids();
	createAlerte(action, patient);
	return action;
}
else if (latestPoids != null && poidsSixMonthago != null && latestPoids.getPoids() < poidsSixMonthago.getPoids() * 0.85 ) {
	String action = "dénutrition sévère car le poids a diminué de plus de 15% en six mois : " + "poids : " + latestPoids.getPoids() + " poids 6 mois avant : " + poidsSixMonthago.getPoids();
	createAlerte(action, patient);
	return action;
}
else if (albumine != null && albumine.getAlbu() <= 30 ) {
	String action = "dénutrition sévère car le niveau d'albumine est inférieur ou égal à 30 : " + "albumine : " + albumine.getAlbu();
	createAlerte(action, patient);
	return action;
}
	else if (latestimc != null && latestimc.getImc() < 18.5 && latestimc.getImc() > 17 ) {
	String action = "dénutrition modérée car IMC est entre 17 et 18.5 : " + "imc : " + latestimc.getImc();
	createAlerte(action, patient);
	return action;
}
else if (latestPoids != null && poidsOneMonthago != null && latestPoids.getPoids() < poidsOneMonthago.getPoids() * 0.95 ) {
	String action = "dénutrition modérée car le poids a diminué de plus de 5% en un mois : " + "poids : " + latestPoids.getPoids() + " poids 1 mois avant : " + poidsOneMonthago.getPoids();
	createAlerte(action, patient);
	return action;
}
else if (latestPoids != null && poidsSixMonthago != null && latestPoids.getPoids() < poidsSixMonthago.getPoids() * 0.9 ) {
	String action = "dénutrition modérée car le poids a diminué de plus de 10% en six mois : " + "poids : " + latestPoids.getPoids() + " poids 6 mois avant : " + poidsSixMonthago.getPoids();
	createAlerte(action, patient);
	return action;
}
else if (albumine != null && albumine.getAlbu() > 30 && albumine.getAlbu() < 35 ) {
	String action = "dénutrition modérée car le niveau d'albumine est entre 30 et 35 : " + "albumine : " + albumine.getAlbu();
	createAlerte(action, patient);
	return action;
}

else {
	return "pas de dénutrition";
}
}

 /**
 * DELETE  /patients/all/:id : delete patient
 *
 * @param id the id of the patient 
 * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
 */
@DeleteMapping("/patients/all/{id}")
public ResponseEntity<Void> deleteAllPatient(@PathVariable Long id) {
    log.debug("REST request to delete Patient : {}", id);
    alerteRepository.deleteByPatient_Id(id);
	imcRepository.deleteByPatient_Id(id);
	albumineRepository.deleteByPatient_Id(id);
	poidsRepository.deleteByPatient_Id(id);
	epaRepository.deleteByPatient_Id(id);
	rappelRepository.deleteByPatient_Id(id);
	noteRepository.deleteByPatient_Id(id);
	patientRepository.deleteById(id);
    return ResponseEntity
        .noContent()
        .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
        .build();
}
	
	}
