import React, { useState, useEffect } from 'react';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getAlertesByUser,toggleVerif } from 'app/entities/alerte/alerte.reducer';
import { Link } from 'react-router-dom';
import './Alerte.css'; // Import your CSS file
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faFileAlt, faCalendar } from '@fortawesome/free-solid-svg-icons';
import DatePicker from 'react-datepicker';
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

  const [selectedDate, setSelectedDate] = useState(null);

  const [showDatePicker, setShowDatePicker] = useState(false);

  const handleCalendarIconClick = () => {
    setShowDatePicker(!showDatePicker);
  };

  const handleDatePickerClose = () => {
    setShowDatePicker(false);
  };

  const filteredAlertes = alertes.filter(alerte => {
    if (filter === 'all') return true;
    if (filter === 'verified' && alerte.verif) return true;
    if (filter === 'unverified' && !alerte.verif) return true;
    return false;
  });
  return (
    <div style={{ minHeight: '1000px'}}>
      <div>
      <select style={{marginRight: '5px'}} onChange={(e) => setFilter(e.target.value)}>
        <option value="all">Toutes les alertes</option>
        <option value="verified">Alertes vérifiées</option>
        <option value="unverified">Alertes non vérifiées</option>
      </select>
      <FontAwesomeIcon icon={faCalendar} onClick={handleCalendarIconClick} />
        {showDatePicker && (
          <DatePicker
            selected={selectedDate}
            onChange={date => setSelectedDate(date)}
            placeholderText='Sélectionnez une date'
            isClearable
          />
        )}
      </div>
      {filteredAlertes.map(alerte => (
  <div key={alerte.id} className="alerte">
    <div className="alerte-icon">⚠️</div>
    <div style={{ display: 'flex'}}>
        <span className="category">Patient : </span> <span style={{fontSize: '1.2em'}}>{alerte.patient.nom} {alerte.patient.prenom}</span>
    </div>
    <div style={{ display: 'flex'}}>
      <span className="category">Action : </span> <span style={{fontSize: '1.2em'}}>{alerte.action}</span>
    </div>
    <div style={{ display: 'flex'}}>
      <span className="category">Date : </span> <span style={{fontSize: '1.2em'}}>{new Date(alerte.date).toLocaleDateString()}</span>
    </div>
    <div style={{ display: 'flex'}}>
      {alerte.patient && <Link to={`/patients/${alerte.patient.id}`} className="alerte-button">
          <FontAwesomeIcon icon={faFileAlt} />
          Voir patient
        </Link>}
      <button className="alerte-check" onClick={() => handleToggleVerif(alerte.id)}>
        {alerte.verif ? '✅' : '⬜'}
      </button>
    </div>
  </div>
))}
    </div>
  );
};

export default Alerte;