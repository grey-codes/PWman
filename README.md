# PWman
Password manager, made as a term project for IS360 at SEMO

A general project design:
- Main screen, uses RecyclerView to display password list, with FAB to add a password.
Use fragment here, thus satisfying that requirement. Fragment swapped out to view fragment after tapping pw.
- Password view fragment. View password, copy it to clipboard when tapped (fragment_detail)
Buttons to edit, and check if it's been pwned
- Password edit fragment (fragment_Add_edit)
- Pwned activity, launched when the pwn button is pressed. Checks haveibeenpwned JSON api to see if password is compromised.
Satisfies JSON requirement.
- Passwords will be stored in SQLite, satisfying that requirement
- Implement password encryption if at all possible.
