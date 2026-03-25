import { Link, Route, Routes } from 'react-router-dom';

const Page = ({ title }: { title: string }) => <div className="text-2xl font-bold">{title}</div>;

export function AppRouter() {
  return (
    <div className="min-h-screen bg-slate-950 text-slate-50 p-6">
      <nav className="flex gap-4 mb-6 text-sky-400">
        <Link to="/">Dashboard</Link>
        <Link to="/seasons">Temporadas</Link>
        <Link to="/competitions">Competiciones</Link>
        <Link to="/mangas">Mangas</Link>
        <Link to="/participants">Participantes</Link>
        <Link to="/catches">Capturas</Link>
      </nav>
      <Routes>
        <Route path="/" element={<Page title="Dashboard" />} />
        <Route path="/seasons" element={<Page title="Temporadas" />} />
        <Route path="/competitions" element={<Page title="Competiciones" />} />
        <Route path="/mangas" element={<Page title="Detalle de manga" />} />
        <Route path="/participants" element={<Page title="Gestión de participantes" />} />
        <Route path="/catches" element={<Page title="Validación de capturas" />} />
      </Routes>
    </div>
  );
}
