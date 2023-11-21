import React, { useState, useEffect } from 'react';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getAlertesByUser,toggleVerif } from 'app/entities/alerte/alerte.reducer';
import { Link } from 'react-router-dom';
import './Alerte.css'; // Import your CSS file

export const Alerte = () => {
  const account = useAppSelector(state => state.authentication.account);
  const dispatch = useAppDispatch();

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
  return (
    <div>
      <h1>Alerte Page</h1>
      {alertes.map(alerte => (
        <div key={alerte.id} className="alerte">
          <div className="alerte-content">
            <div className="alerte-icon">⚠️</div>
            <div>
              <h2>Alerte dénutrition</h2>
              <p>Action: {alerte.action}</p>
              <p>Patient: {alerte.patient.nom} {alerte.patient.prenom}</p>
              <p>Date: {alerte.date}</p>
            </div>
            <button className="alerte-check" onClick={() => handleToggleVerif(alerte.id)}>
            {alerte.verif ? '✅' : '⬜'}
            </button>
          </div>
          <Link to={`/patients/${alerte.patient.id}`} className="alerte-button">Voir patient</Link>
        </div>
      ))}
    </div>
  );
};

export default Alerte;