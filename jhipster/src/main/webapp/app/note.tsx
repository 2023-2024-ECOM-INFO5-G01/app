
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
import { NoteCreate } from './NoteCreate';
import Modal from 'react-modal';
import { NoteEdit } from './NoteUpdate';
import { set, update } from 'lodash';
export const Notes = ({ idprops }: { idprops: string }) => {
  const account = useAppSelector(state => state.authentication.account);
  const dispatch = useAppDispatch();
  const { id } = useParams<'id'>();
  const [filter, setFilter] = useState('all'); 
  const [selectedDate, setSelectedDate] = useState(null); 
  const [modal, setModal] = useState(false);
  const [modal2, setModal2] = useState(false);

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
}, [account.login, dispatch,modal,modal2]);

  const [showDatePicker, setShowDatePicker] = useState(false);

  const handleCalendarIconClick = () => {
    setShowDatePicker(!showDatePicker);
  };

  const handleDatePickerClose = () => {
    setShowDatePicker(false);
  };
  const [creatingNote, setCreatingNote] = useState(false);
  const toggle = () => {
    if (modal) {
      setCreatingNote(false);
    }
    setModal(!modal);
  };
    const toggle2 = () => {
    if (modal2) {
      setEditingNote(null);
    }
    setModal2(!modal2);
    };
    const [editingNote, setEditingNote] = useState(null);

  return (
    <div>
      {notes.map(note => (
        <div key={note.id} className="note">
          <div className="note-content">
            <h3 className="note-title">Titre:{note.titre}</h3>
            <p>{note.note}</p>
          </div>
          <div className="note-buttons">
  {editingNote === note.id ? (
    <NoteEdit modal={modal2} toggle={toggle2} patientId={idprops} idnote={note.id} />
  ) : (
    <button className="note-button" onClick={() => {setEditingNote(note.id);setModal2(true);}}>
      <FontAwesomeIcon icon={faPencilAlt} />
    </button>
  )}
  <button className="note-button">
    <FontAwesomeIcon icon={faTrash} />
  </button>
</div>
        </div>
      ))}
     {creatingNote ? (
        <NoteCreate modal={modal} toggle={toggle} patientId={idprops} />
      ) : (
        <button className="add-note-button" onClick={() => {setCreatingNote(true);setModal(true);}}>
          <FontAwesomeIcon icon={faPlus} />
        </button>
      )}
    </div>
  );
}

export default Notes;