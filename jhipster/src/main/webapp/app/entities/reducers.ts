import ePA from 'app/entities/epa/epa.reducer';
import ehpad from 'app/entities/ehpad/ehpad.reducer';
import patient from 'app/entities/patient/patient.reducer';
import albumine from 'app/entities/albumine/albumine.reducer';
import rappel from 'app/entities/rappel/rappel.reducer';
import poids from 'app/entities/poids/poids.reducer';
import iMC from 'app/entities/imc/imc.reducer';
import repas from 'app/entities/repas/repas.reducer';
import note from 'app/entities/note/note.reducer';
import aliment from 'app/entities/aliment/aliment.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  ePA,
  ehpad,
  patient,
  albumine,
  rappel,
  poids,
  iMC,
  repas,
  note,
  aliment,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
