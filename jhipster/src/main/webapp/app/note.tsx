
import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { Link, useLocation, useNavigate, useParams } from 'react-router-dom';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import './Note.css';

import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faPlus, faPencilAlt, faTrash } from '@fortawesome/free-solid-svg-icons';
import { getNotesByPatientAndUser } from 'app/entities/note/note.reducer';
export const Notes = ({ idprops }: { idprops: string }) => {
  const account = useAppSelector(state => state.authentication.account);
  const dispatch = useAppDispatch();
  const { id } = useParams<'id'>();
  const [filter, setFilter] = useState('all'); 
  const [selectedDate, setSelectedDate] = useState(null); 

  const [notes, setNotes] = useState([]);
useEffect(() => {
    if (account && account.login) {
        dispatch(getNotesByPatientAndUser({ id: idprops, login: account.login }))
            .then(response => {
                setNotes((response.payload as any).data);
                console.log(notes);
            })
            .catch(error => {
                console.error('Une erreur s\'est produite :', error);
            });
    }
}, [account.login, dispatch]);

  const [showDatePicker, setShowDatePicker] = useState(false);

  const handleCalendarIconClick = () => {
    setShowDatePicker(!showDatePicker);
  };

  const handleDatePickerClose = () => {
    setShowDatePicker(false);
  };
  
  return (
    <div>
      {notes.map(note => (
        <div key={note.id} className="note">
          <div className="note-content">
            <h3 className="note-title">Titre:{note.titre}</h3>
            <p>{note.note}</p>
          </div>
          <div className="note-buttons">
            <button className="note-button">
              <FontAwesomeIcon icon={faPencilAlt} />
            </button>
            <button className="note-button">
              <FontAwesomeIcon icon={faTrash} />
            </button>
          </div>
        </div>
      ))}
      <button className="add-note-button">
        <FontAwesomeIcon icon={faPlus} />
      </button>
    </div>
  );
}

export default Notes;