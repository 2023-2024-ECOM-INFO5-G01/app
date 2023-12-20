import React, { useState, useEffect } from 'react';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getAlertesByUser,toggleVerif } from 'app/entities/alerte/alerte.reducer';
import { Link } from 'react-router-dom';
import './Alerte.css'; // Import your CSS file
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faFileAlt } from '@fortawesome/free-solid-svg-icons';
export const Alerte = () => {
  const account = useAppSelector(state => state.authentication.account);
  const dispatch = useAppDispatch();
  const [filter, setFilter] = useState('all'); 

  const [alertes, setAlertes] = useState([]);
  useEffect(() => {
    if (account && account.login) {
      dispatch(getAlertesByUser(account.login))
      .then(response => {
        setAlertes((response.payload as any).data);
        console.log(alertes);
      })
      .catch(error => {
        console.error('Une erreur s\'est produite :', error);
      });
    }
  }, [account.login, dispatch]);

  const handleToggleVerif = (id: string | number) => { // New function to handle click
    dispatch(toggleVerif(id))
    .then(() => {
      // Refresh the alertes after toggling verif
      if (account && account.login) {
        dispatch(getAlertesByUser(account.login))
        .then(response => {
          setAlertes((response.payload as any).data);
        });
      }
    });
  };

  const filteredAlertes = alertes.filter(alerte => {
    if (filter === 'all') return true;
    if (filter === 'verified' && alerte.verif) return true;
    if (filter === 'unverified' && !alerte.verif) return true;
    return false;
  });
  return (
    <div>
      <div>
      <select onChange={(e) => setFilter(e.target.value)}>
        <option value="all">Toutes les alertes</option>
        <option value="verified">Alertes vérifiées</option>
        <option value="unverified">Alertes non vérifiées</option>
      </select>
      </div>
      {filteredAlertes.map(alerte => (
  <div key={alerte.id} className="alerte">
    <div className="alerte-icon">⚠️</div>
    <div className="alerte-content">
      <div className="alerte-text1">
          <p>Patient: {alerte.patient.nom} {alerte.patient.prenom}</p>
          </div>
      <div className="alerte-text2">
          <p>Action: {alerte.action}</p>
          </div>
      <div className="alerte-text3">
          <p>Date: {alerte.date}</p>
      </div>
    </div>
    {alerte.patient && <Link to={`/patients/${alerte.patient.id}`} className="alerte-button">
        <FontAwesomeIcon icon={faFileAlt} />
        Voir patient
      </Link>}
    <button className="alerte-check" onClick={() => handleToggleVerif(alerte.id)}>
      {alerte.verif ? '✅' : '⬜'}
    </button>
  </div>
))}
    </div>
  );
};

export default Alerte;